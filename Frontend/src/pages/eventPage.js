import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import EventClient from "../api/eventClient";

class EventPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderEvent', 'renderEvents', 'autofill', 'updateEvent',
            'deleteEvent', 'getEventsToday', 'getEventsByDate', 'getEventsByDescription', 'getEventsByCategory',
            'getEventsByUsername', 'getEventsByEventName', 'getEventsByVenueId'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('create-event-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-event-form').addEventListener('submit', this.updateEvent);
        document.getElementById('delete-event-form').addEventListener('submit', this.deleteEvent);
        document.getElementById('today-search').addEventListener('click', this.getEventsToday);
        document.getElementById('id-search').addEventListener('click', this.onGet);
        document.getElementById('venue-id-search').addEventListener('click', this.getEventsByVenueId);
        document.getElementById('event-name-search').addEventListener('click', this.getEventsByEventName);
        document.getElementById('username-search').addEventListener('click', this.getEventsByUsername);
        document.getElementById('category-search').addEventListener('click', this.getEventsByCategory);
        document.getElementById('description-search').addEventListener('click', this.getEventsByDescription);
        document.getElementById('date-search').addEventListener('click', this.getEventsByDate);
        document.getElementById('autofill').addEventListener('click', this.autofill);
        this.client = new EventClient();

        this.renderEvents();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderEvent(event) {
        let resultArea = document.getElementById("public-events");

        if (event) {
            resultArea.innerHTML = `
                <div class="results">
                <h3 class="name-result"><a href="rsvp.html?id=${event.id}">${event.event_name}</a></h3>
                <p class="id-result">Id: ${event.id}</p>
                <p class="host-result">Host: ${event.username}</p>
                <p class="category-result">${event.category}</p>
                <p class="dates-result">${event.start_date} to ${event.end_date}</p>
                <p class="desc-result">${event.description}</p>
                <p class="venue-result">Venue: ${event.venue_id}</p>
                </div>
                `
        } else {
            resultArea.innerHTML = "No Such Event";
        }
    }

    async renderEvents(list) {
        let resultArea = document.getElementById("public-events");
        let events = null;

        if (list) {
            events = list;
        } else {
            events = await this.client.getEvents(this.errorHandler);
        }

        resultArea.innerHTML = "";
        if (events) {
            for(const event of events){
                resultArea.innerHTML += `
                    <div class="results">
                    <h3 class="name-result"><a href="rsvp.html?id=${event.id}">${event.event_name}</a></h3>
                    <p class="id-result">Id: ${event.id}</p>
                    <p class="host-result">Host: ${event.username}</p>
                    <p class="category-result">${event.category}</p>
                    <p class="dates-result">${event.start_date} to ${event.end_date}</p>
                    <p class="desc-result">${event.description}</p>
                    <p class="venue-result">Venue: ${event.venue_id}</p>
                    </div>
                    `
            }
        } else {
            resultArea.innerHTML = "No Events";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("search-bar-field").value;
        this.dataStore.set("event", null);

        let result = await this.client.getEventById(id, this.errorHandler);
        this.dataStore.set("event", result);
        this.renderEvent(result);
        if (result) {
            this.showMessage(`Got ${result.event_name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        event.preventDefault();
        this.dataStore.set("event", null);

        let venue_id = document.getElementById("create-venue-id-field").value;
        let event_name = document.getElementById("create-event-name-field").value;
        let category = document.getElementById("create-category-field").value;
        let description = document.getElementById("create-description-field").value;
        let start_date = document.getElementById("create-start-date-field").value;
        let end_date = document.getElementById("create-end-date-field").value;
        let username = document.getElementById("create-username-field").value;

        const createdEvent = await this.client.createEvent(event_name, username, venue_id, description,
            start_date, end_date, category, this.errorHandler);
        this.dataStore.set("event", createdEvent);

        if (createdEvent) {
            this.showMessage(`Created ${createdEvent.event_name}!`);
        } else {
            this.errorHandler("Error creating! Try again...");
        }
        this.renderEvents();
    }

    async updateEvent(event) {
        event.preventDefault();
        this.dataStore.set("event", null);

        let id = document.getElementById("update-id-field").value;
        let venue_id = document.getElementById("update-venue-id-field").value;
        let event_name = document.getElementById("update-event-name-field").value;
        let category = document.getElementById("update-category-field").value;
        let description = document.getElementById("update-description-field").value;
        let start_date = document.getElementById("update-start-date-field").value;
        let end_date = document.getElementById("update-end-date-field").value;
        let username = document.getElementById("update-username-field").value;

        const updatedEvent = await this.client.updateEvent(id, event_name, username, venue_id, description,
            start_date, end_date, category, this.errorHandler);
        this.dataStore.set("event", updatedEvent);

        if (updatedEvent) {
            this.showMessage(`Updated ${updatedEvent.event_name}!`);
        } else {
            this.errorHandler("Error updating! Try again...");
        }
        this.renderEvents();
    }

    async deleteEvent(event) {
        event.preventDefault();
        this.dataStore.set("event", null);

        let id = document.getElementById("delete-id-field").value;

        const result = await this.client.deleteEvent(id, this.errorHandler);
        this.dataStore.set("event", result);

        if (result) {
            this.showMessage(`${result.event_name} has been Deleted!`);
        } else {
            this.errorHandler("Error deleting! Try again...");
        }
        this.renderEvents();
    }

    async getEventsToday(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let result = await this.client.getEventsToday(this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events Today!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing today's events! Try again...");
        }
    }

    async getEventsByDate(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("date-search-field").value;
        let result = await this.client.getEventsByDate(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events running on "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing today's events! Try again...");
        }
    }

    async getEventsByVenueId(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("search-bar-field").value;
        let result = await this.client.getEventsByVenueId(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events with venue id "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing venueId search! Try again...");
        }
    }

    async getEventsByEventName(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("search-bar-field").value;
        let result = await this.client.getEventsByEventName(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events with names containing "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing event name search! Try again...");
        }
    }

    async getEventsByUsername(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("search-bar-field").value;
        let result = await this.client.getEventsByUsername(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events from users named "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing username search! Try again...");
        }
    }

    async getEventsByCategory(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("search-bar-field").value;
        let result = await this.client.getEventsByCategory(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events in category "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing category search! Try again...");
        }
    }

    async getEventsByDescription(event) {
        event.preventDefault();
        this.dataStore.set("events", null);

        let searchValue = document.getElementById("search-bar-field").value;
        let result = await this.client.getEventsByDescription(searchValue, this.errorHandler);
        this.dataStore.set("events", result);
        if (result) {
            this.showMessage(`Showing Events with description containing "${searchValue}"!`)
            this.renderEvents(result);
        } else {
            this.errorHandler("Error showing description search! Try again...");
        }
    }

    async autofill(event) {
        event.preventDefault();
        document.getElementById("autofillStatus").innerHTML = "Loading...";
        const result = await this.client.getEventById(document.getElementById("update-id-field").value, this.errorHandler);

        document.getElementById("update-id-field").value = result.id;
        document.getElementById("update-venue-id-field").value = result.venue_id;
        document.getElementById("update-event-name-field").value = result.event_name;
        document.getElementById("update-category-field").value = result.category;
        document.getElementById("update-description-field").value = result.description;
        document.getElementById("update-start-date-field").value = result.start_date;
        document.getElementById("update-end-date-field").value = result.end_date;
        document.getElementById("update-username-field").value = result.username;

        document.getElementById("autofillStatus").innerHTML = "";
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const eventPage = new EventPage();
    eventPage.mount();
};

window.addEventListener('DOMContentLoaded', main);