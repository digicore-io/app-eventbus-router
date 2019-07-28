import { EvendtDao } from "./dao/event-dao";
import { EventRequest, CompanyAppEvent, CompanyAppConfig, AppConfig } from "./classes";
import { EbayService } from "./ebay-service";
const uuidv4 = require('uuid/v4');
const AWS = require('aws-sdk');
AWS.config.update({ region: 'us-west-2' });
const axios = require('axios');
const eventDao = new EvendtDao();
const ebayService = new EbayService();

export class CoreService {

    async processMessage(item: any) {
        let eventRequest: EventRequest = <EventRequest>JSON.parse(item.body);
        let companyAppConfig = <CompanyAppConfig>await eventDao.getCompanyAppConfig(eventRequest.companyId, eventRequest.applicationId);
        let appConfig = <AppConfig>await eventDao.getApplication(eventRequest.applicationId);
        
        console.log('config', companyAppConfig)
        if (!companyAppConfig || !companyAppConfig.paymentProfileId || !companyAppConfig.returnProfileId || !companyAppConfig.shippingProfileId)
            throw new Error('Could not find eBay Policy IDs');
        
        await this.createUpdateEbayProduct(eventRequest, companyAppConfig, appConfig);
    }

    /**
     * First check if we've have an existing ebay offer id in the store
     * Then either create or update the ebay offer
     * 
     * @param eventRequest 
     * @param companyApp 
     */
    async createUpdateEbayProduct(eventRequest: EventRequest, config: CompanyAppConfig, appConfig:AppConfig) {
        return new Promise(async function (resolve, reject) {
            ebayService.addProduct(eventRequest, config, appConfig);

            //TODO Put back
        //     let entity = await axios.get('http://localhost:3010/app-entity?entityId=' + eventRequest.payload.productId + '&companyId=' + eventRequest.companyId)
        //         .then(function (response) {
        //             // handle success
        //             console.log('Found entity', response);
        //             ebayService.updateProduct(eventRequest, config, response);
        //         })
        //         .catch(function (error) {
        //             if(error.response.status == 404){
        //                 //Creating new
        //                 console.log('entity not found');
        //                 ebayService.addProduct(eventRequest, config, appConfig);
        //             }else
        //                 resolve(error);
        //         })
        });
    }
}

