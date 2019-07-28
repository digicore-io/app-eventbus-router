import {Route} from './classes'
import { EbayService } from './ebay-service';
import { CoreService } from './core-service';
const coreService = new CoreService();

export class Routes{
    public handleGetRequest(event:any, route:Route){
        switch(route.resource){
            case '/test' :{
                let event:any = {};
                event.Records = [];
                let record:any = {};
                record.body = '{"companyId":"5555","event":"ECOM_PRODUCT_UPDATED","applicationId":"d21ee17b-c1cb-46fe-9ebb-182e27bd7075","payload":{"productId":"1234"},"companyAppEventId":"78f9a93e-953a-423b-b45e-9e6679fe0de1"}';
                event.Records.push(record);
                
                coreService.processMessage(record);
                break;
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