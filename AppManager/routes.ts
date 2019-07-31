import { AppEntityService } from './service/app-entity-service';
import { Location, LocationRequest } from './classes'
import { Route } from './classes'
import { AppEntity } from './interfaces';
const appEntityService = new AppEntityService();


export class Routes {
    public async handleGetRequest(event: any, route: Route) {

        switch (route.resource) {
            case '/apps/{appId}/companies/{companyId}/entities/{entityId}': {
                return appEntityService.getAppEntity(route.pathParameters.appId, route.pathParameters.companyId, route.pathParameters.entityId);
            }
            default: {
                console.log('Unknown Route Resource', JSON.stringify(route))
                throw 'Unknown Route Resource in GET request: ' + route.resource;
            }

        }

    }

    public async handlePostRequest(event: any, route: Route) {
        let jsonObject: any = JSON.parse(event.body);
        switch (route.resource) {
            default: {
                console.log('Unknown Route Resource in PUT Request', JSON.stringify(route))
                throw 'Unknown Route Resource in PUT Request: ' + route.resource;
            }
        }

    }

    public async handlePutRequest(event: any, route: Route) {
        let jsonObject: any = JSON.parse(event.body);
        switch (route.resource) {
            case '/apps/{appId}/companies/{companyId}/entities/{entityId}': {
                return await appEntityService.saveAppEntity(route.pathParameters.appId, route.pathParameters.companyId,
                    route.pathParameters.entityId, <AppEntity>jsonObject);
            }
            default: {
                console.log('Unknown Route Resource in PUT Request', JSON.stringify(route))
                throw 'Unknown Route Resource in PUT Request: ' + route.resource;
            }
        }
    }

    public async handleDeleteRequest(event: any, route: Route) {
        switch (route.resource) {

            default: {
                console.log('Unknown Route Resource in DELETE Request', JSON.stringify(route))
                throw 'Unknown Route Resource in DELETE Request: ' + route.resource;
            }
        }
    }

}