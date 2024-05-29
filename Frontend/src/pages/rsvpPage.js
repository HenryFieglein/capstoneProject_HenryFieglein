import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RsvpClient from "../api/rsvpClient";

class RsvpPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['addGuest', 'renderGuest', 'renderGuests'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('rsvp-form').addEventListener('submit', this.addGuest);
        this.client = new RsvpClient();

        this.renderGuests();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderGuest() {
        let resultArea = document.getElementById("result-info");

        const event = this.dataStore.get("event");

        if (event) {
            resultArea.innerHTML = `
                <div>ID: ${event.id}</div>
                <div>Name: ${event.name}</div>
            `
        } else {
            resultArea.innerHTML = "No Such Event";
        }
    }

    async renderGuests() {
        let resultArea = document.getElementById("guest-list");
        const queries = new Proxy(new URLSearchParams(window.location.search), {
            get: (params, val) => params.get(val),
        });
        let id = queries.id;

        const guests = await this.client.getGuests(id, this.errorHandler);
        resultArea.innerHTML = "";
        if (guests) {
            for(const guest of guests){
                resultArea.innerHTML += `
                    <div class="rsvp-results">
                    <p class="name-result">${guest.name}</p>
                    </div>
                    `
            }
        } else {
            resultArea.innerHTML = "No Guests";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async addGuest(event) {
        event.preventDefault();
        this.dataStore.set("guest", null);

        let name = document.getElementById("name-field").value;
        const queries = new Proxy(new URLSearchParams(window.location.search), {
            get: (params, val) => params.get(val),
        });
        let id = queries.id;

        const createdGuest = await this.client.rsvp(name, id, this.errorHandler);
        this.dataStore.set("guest", createdGuest);

        if (createdGuest) {
            this.showMessage(`${createdGuest.name} has RSVPd!`);
        } else {
            this.errorHandler("Error RSVPing! Try again...");
        }
        this.renderGuests();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const rsvpPage = new RsvpPage();
    rsvpPage.mount();
};

window.addEventListener('DOMContentLoaded', main);