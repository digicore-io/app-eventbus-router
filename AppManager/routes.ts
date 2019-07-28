import {AppEntityService} from './service/app-entity-service';
import {Location,LocationRequest} from './classes'
import {Route} from './classes'
import { AppEntity } from './interfaces';
const appEntityService = new AppEntityService();


export class Routes{
    public handleGetRequest(event:any, route:Route){
        switch(route.resource){
            case '/app-entity':{
                return appEntityService.getAppEntity(route.queryParameters.entityId, route.queryParameters.companyId);
            }
            default:{
                console.log('Unknown Route Resource', JSON.stringify(route))
                throw 'Unknown Route Resource in GET request: ' + route.resource;
            }

        }
        
    }

    public handlePostRequest(event:any, route:Route){
        let jsonObject:any = JSON.parse(event.body);
        switch(route.resource){
           
            default:{
                console.log('Unknown Route Resource in PUT Request', JSON.stringify(route))
                throw 'Unknown Route Resource in PUT Request: ' + route.resource;
            }
        }
        
    }

    public async handlePutRequest(event:any, route:Route){
        let jsonObject:any = JSON.parse(event.body);
        switch(route.resource){
            case '/app-entity':{
                console.log('test')
                return await appEntityService.saveAppEntity(<AppEntity>jsonObject);
                //break;
            }
            default:{
                console.log('Unknown Route Resource in PUT Request', JSON.stringify(route))
                throw 'Unknown Route Resource in PUT Request: ' + route.resource;
            }
        }
    }

    public handleDeleteRequest(event:any, route:Route){
        switch(route.resource){
            
            default:{
                console.log('Unknown Route Resource in DELETE Request', JSON.stringify(route))
                throw 'Unknown Route Resource in DELETE Request: ' + route.resource;
            }
        }
    }
    
}