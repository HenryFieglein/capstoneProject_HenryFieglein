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
export default class RsvpClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getAllVenues', 'createVenue', 'getAllAvailableVenues', 'updateVenue',
            'getVenueById', 'deleteVenue'];
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

    async getAllVenues(errorCallback) {
        try {
            const response = await this.client.get(`/venues`);
            return response.data;
        } catch (error) {
            this.handleError("getAllVenues", error, errorCallback)
        }
    }

    async getAllAvailableVenues(errorCallback) {
        try {
            const response = await this.client.get(`/venues/open`);
            return response.data;
        } catch (error) {
            this.handleError("getAllAvailableVenues", error, errorCallback)
        }
    }

    async createVenue(venue_name, description, address, status, event_capacity, phone, website, email, errorCallback) {
        try {
            const response = await this.client.post(`/venues`, {
                venue_name: venue_name,
                description: description,
                address: address,
                status: status,
                event_capacity: event_capacity,
                phone: phone,
                website: website,
                email: email
            });
            return response.data;
        } catch (error) {
            this.handleError("createVenue", error, errorCallback);
        }
    }

    async updateVenue(id, venue_name, description, address, status, event_capacity, phone, website, email, errorCallback) {
        try {
            const response = await this.client.post(`/venues/${id}`, {
                venue_name: venue_name,
                description: description,
                address: address,
                status: status,
                event_capacity: event_capacity,
                phone: phone,
                website: website,
                email: email
            });
            return response.data;
        } catch (error) {
            this.handleError("updateVenue", error, errorCallback);
        }
    }

    async getVenueById(id, errorCallback) {
        try {
            const response = await this.client.get(`/venues/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("getVenueById", error, errorCallback)
        }
    }

    async deleteVenue(id, errorCallback) {
        try {
            const response = await this.client.delete(`/venues/${id}`);
            return response.status;
        } catch (error) {
            this.handleError("deleteVenue", error, errorCallback)
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
