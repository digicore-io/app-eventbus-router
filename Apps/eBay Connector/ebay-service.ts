import { EventRequest, CompanyAppEvent, CompanyAppConfig, AppConfig } from "./classes";
import { EbayAuthService } from "./ebay-auth-service";
const AWS = require('aws-sdk');
AWS.config.update({ region: 'us-west-2' });
const axios = require('axios');
const ebayAuthService = new EbayAuthService();
const BASE_URL = "https://api.sandbox.ebay.com/";

export class EbayService {

    async addProduct(eventRequest: EventRequest, companyAppConfig: CompanyAppConfig, appConfig: AppConfig) {
        console.log('addProduct')
        let token = ebayAuthService.getToken(eventRequest, companyAppConfig, appConfig);
        
    }

    

    /**
     * First check if we've have an existing ebay offer id in the store
     * Then either create or update the ebay offer
     * 
     * @param eventRequest 
     * @param companyApp 
     */
    async updateProduct(eventRequest: EventRequest, companyAppConfig: CompanyAppConfig, appConfig: AppConfig, entity: any) {
        console.log('updateProduct')
        let token = ebayAuthService.getToken(eventRequest, companyAppConfig, appConfig);
    }
}

