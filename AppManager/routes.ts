import { AppEntityService } from './service/app-entity-service';
import { Location, LocationRequest, Error } from './classes'
import { Route } from './classes'
import { AppEntity } from './interfaces';
const appEntityService = new AppEntityService();


export class Routes {
    public async handleGetRequest(event: any, route: Route) {

        switch (route.resource) {
            case '/applications/{applicationId}/companies/{companyId}/entities/{entityId}': {
                return appEntityService.getAppEntity(route.pathParameters.applicationId, route.pathParameters.companyId, route.pathParameters.entityId);
            }
            default: {
                console.log('Unknown Route Resource in GET request', JSON.stringify(route))
                throw new Error(404, 'Unknown Route Resource in GET request: ' + route.resource);
            }

        }

    }

    public async handlePostRequest(event: any, route: Route) {
        let jsonObject: any = JSON.parse(event.body);
        switch (route.resource) {
            default: {
                console.log('Unknown Route Resource in POST Request', JSON.stringify(route))
                throw new Error(404, 'Unknown Route Resource in POST request: ' + route.resource);
            }
        }

    }

    public async handlePutRequest(event: any, route: Route) {
        let jsonObject: any = JSON.parse(event.body);
        switch (route.resource) {
            case '/applications/{applicationId}/companies/{companyId}/entities/{entityId}': {
                await appEntityService.saveAppEntity(route.pathParameters.applicationId, route.pathParameters.companyId,
                    route.pathParameters.entityId, <AppEntity>jsonObject);
                break;
            }
            default: {
                console.log('Unknown Route Resource in PUT Request', JSON.stringify(route))
                throw new Error(404, 'Unknown Route Resource in PUT request: ' + route.resource);
            }
        }
    }

    public async handleDeleteRequest(event: any, route: Route) {
        switch (route.resource) {

            default: {
                console.log('Unknown Route Resource in DELETE Request', JSON.stringify(route))
                throw new Error(404, 'Unknown Route Resource in DELETE request: ' + route.resource);
            }
        }
    }

}