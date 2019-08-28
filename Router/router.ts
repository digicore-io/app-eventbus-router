/**
 * Not need to customise this. Edit the routes in routes.ts
 */
import {Routes} from './routes';
import { Route } from '../../NodeJS-Common/common-classes';

const routes = new Routes();

export class Router{
    public async handleRoute(event){
        let route = new Route(event.requestContext.httpMethod, event.resource, event.pathParameters, event.queryStringParameters);        

        switch (route.method){
            case 'GET': {
                return await routes.handleGetRequest(event, route);    
            }
            case 'POST': {
                return await routes.handlePostRequest(event, route);    
            }
            case 'PUT': {
                return await routes.handlePutRequest(event, route);    
            }
            case 'DELETE': {
                return await routes.handleDeleteRequest(event, route);    
            }
            default:{
                console.log('Unknown Route Method', JSON.stringify(route))
                throw 'Unknown Route Method'
            }
                
        }
    }

    
}