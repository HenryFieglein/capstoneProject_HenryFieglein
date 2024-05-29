import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import VenueClient from "../api/venueClient";

class VenuePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['createVenue', 'renderVenue', 'renderVenues', 'updateVenue', 'autofill',
            'deleteVenue'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('new-venue-form').addEventListener('submit', this.createVenue);
        document.getElementById('update-venue-form').addEventListener('submit', this.updateVenue);
        document.getElementById('delete-venue-form').addEventListener('submit', this.deleteVenue);
        document.getElementById('onlyAvailable').addEventListener('change', this.renderVenues);
        document.getElementById('autofill').addEventListener('click', this.autofill);
        this.client = new VenueClient();

        this.renderVenues();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderVenue() {
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

    async renderVenues() {
        let resultArea = document.getElementById("venue-list");

        let venues = null;
        if (document.getElementById("onlyAvailable").checked == true) {
            venues = await this.client.getAllAvailableVenues(this.errorHandler);
        } else {
            venues = await this.client.getAllVenues(this.errorHandler);
        }
        resultArea.innerHTML = "";
        if (venues) {
            for(const venue of venues){
                resultArea.innerHTML += `
                    <div class="venue-results">
                    <h2><p class="name-result">${venue.name}</p></h2>
                    <p class="venue-id-result">Id: ${venue.id}</p>
                    <p class="desc-result">${venue.description}</p>
                    <p class="address-result">${venue.address}</p>
                    <p class="status-result">Status: ${venue.status}</p>
                    <p class="event-capacity-result">Event Capacity: ${venue.event_capacity}</p>
                    `
                if (venue.phone) {
                    resultArea.innerHTML += `
                        <p class="phone-result">Phone: ${venue.phone}</p>
                        `
                }
                if (venue.website) {
                    resultArea.innerHTML += `
                        <p class="website-result">Website: <a href="${venue.website}">${venue.website}</a></p>
                        `
                }
                if (venue.email) {
                    resultArea.innerHTML += `
                        <p class="email-result">Email: ${venue.email}</p>
                        `
                }
                resultArea.innerHTML += `</div>`
            }
        } else {
            resultArea.innerHTML = "No Venues";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async createVenue(event) {
        event.preventDefault();
        this.dataStore.set("venue", null);

        let venue_name = document.getElementById("venue-name-field").value;
        let description = document.getElementById("description-field").value;
        let address = document.getElementById("address-field").value;
        let status = document.getElementById("status-field").value;
        let event_capacity = document.getElementById("event-capacity-field").value;
        let phone = document.getElementById("phone-field").value;
        let website = document.getElementById("website-field").value;
        let email = document.getElementById("email-field").value;

        const createdVenue = await this.client.createVenue(venue_name, description, address, status, event_capacity,
            phone, website, email, this.errorHandler);
        this.dataStore.set("venue", createdVenue);

        if (createdVenue) {
            this.showMessage(`${createdVenue.name} has been Created!`);
        } else {
            this.errorHandler("Error Creating Venue! Try again...");
        }
        this.renderVenues();
    }

     async updateVenue(event) {
        event.preventDefault();
        this.dataStore.set("venue", null);

        let id = document.getElementById("update-id-field").value;
        let venue_name = document.getElementById("update-venue-name-field").value;
        let description = document.getElementById("update-description-field").value;
        let address = document.getElementById("update-address-field").value;
        let status = document.getElementById("update-status-field").value;
        let event_capacity = document.getElementById("update-event-capacity-field").value;
        let phone = document.getElementById("update-phone-field").value;
        let website = document.getElementById("update-website-field").value;
        let email = document.getElementById("update-email-field").value;

        const updatedVenue = await this.client.updateVenue(id, venue_name, description, address, status,
            event_capacity, phone, website, email, this.errorHandler);
        this.dataStore.set("venue", updatedVenue);

        if (updatedVenue) {
            this.showMessage(`${updatedVenue.name} has been Updated!`);
        } else {
            this.errorHandler("Error Updating Venue! Try again...");
        }
        this.renderVenues();
    }

    async deleteVenue(event) {
        event.preventDefault();
        this.dataStore.set("venue", null);

        let id = document.getElementById("delete-id-field").value;

        const result = await this.client.deleteVenue(id, this.errorHandler);
        this.dataStore.set("venue", result);

        if (result) {
            this.showMessage(`${result.name} has been Deleted!`);
        } else {
            this.errorHandler("Error Deleting Venue...");
        }
        this.renderVenues();
    }

    async autofill(event){
        event.preventDefault();
        document.getElementById("autofillStatus").innerHTML = "Loading...";
        const venue = await this.client.getVenueById(document.getElementById("update-id-field").value, this.errorHandler);

        document.getElementById("update-venue-name-field").value = venue.name;
        document.getElementById("update-description-field").value = venue.description;
        document.getElementById("update-address-field").value = venue.address;
        document.getElementById("update-status-field").value = venue.status;
        document.getElementById("update-event-capacity-field").value = venue.event_capacity;
        if (venue.phone) {
            document.getElementById("update-phone-field").value = venue.phone;
        } else {
            document.getElementById("update-phone-field").value = "";
        }
        if (venue.website) {
            document.getElementById("update-website-field").value = venue.website;
        } else {
            document.getElementById("update-website-field").value = "";
        }
        if (venue.email) {
            document.getElementById("update-email-field").value = venue.email;
        } else {
            document.getElementById("update-email-field").value = "";
        }

        document.getElementById("autofillStatus").innerHTML = "";
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const venuePage = new VenuePage();
    venuePage.mount();
};

window.addEventListener('DOMContentLoaded', main);