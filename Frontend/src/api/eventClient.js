import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class EventClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getEventById', 'getEvents', 'createEvent', 'updateEvent', 'deleteEvent',
            'getEventsToday', 'getEventsByDate', 'getEventsByEventName', 'getEventsByVenueId', 'getEventsByUsername',
             'getEventsByCategory', 'getEventsByDescription'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getEventById(id, errorCallback) {
        try {
            const response = await this.client.get(`/events/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventById", error, errorCallback)
        }
    }

    async getEvents(errorCallback) {
        try {
            const response = await this.client.get(`/events`);
            return response.data;
        } catch (error) {
            this.handleError("getEvents", error, errorCallback)
        }
    }

    async createEvent(event_name, username, venue_id, description, start_date, end_date, category, errorCallback) {
        try {
            const response = await this.client.post(`events`, {
                event_name: event_name,
                username: username,
                venue_id: venue_id,
                description: description,
                start_date: start_date,
                end_date: end_date,
                category: category
            });
            return response.data;
        } catch (error) {
            this.handleError("createEvent", error, errorCallback);
        }
    }

    async updateEvent(id, event_name, username, venue_id, description, start_date, end_date, category, errorCallback) {
        try {
            const response = await this.client.post(`/events/${id}`, {
                event_name: event_name,
                username: username,
                venue_id: venue_id,
                description: description,
                start_date: start_date,
                end_date: end_date,
                category: category
            });
            return response.data;
        } catch (error) {
            this.handleError("updateEvent", error, errorCallback);
        }
    }

    async deleteEvent(id, errorCallback) {
        try {
            const response = await this.client.delete(`/events/${id}`);
            return response.status;
        } catch (error) {
            this.handleError("deleteEvent", error, errorCallback)
        }
    }

    async getEventsToday(errorCallback) {
        try {
            const response = await this.client.get(`/events/today`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsToday", error, errorCallback)
        }
    }

    async getEventsByDate(date, errorCallback) {
        try {
            const response = await this.client.get(`/events/date/${date}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByDate", error, errorCallback)
        }
    }

    async getEventsByVenueId(id, errorCallback) {
        try {
            const response = await this.client.get(`/events/venueId/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByVenueId", error, errorCallback)
        }
    }

    async getEventsByEventName(eventName, errorCallback) {
        try {
            const response = await this.client.get(`/events/eventName/${eventName}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByEventName", error, errorCallback)
        }
    }

    async getEventsByUsername(username, errorCallback) {
        try {
            const response = await this.client.get(`/events/username/${username}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByUsername", error, errorCallback)
        }
    }

    async getEventsByCategory(category, errorCallback) {
        try {
            const response = await this.client.get(`/events/category/${category}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByCategory", error, errorCallback)
        }
    }

    async getEventsByDescription(description, errorCallback) {
        try {
            const response = await this.client.get(`/events/description/${description}`);
            return response.data;
        } catch (error) {
            this.handleError("getEventsByDescription", error, errorCallback)
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
