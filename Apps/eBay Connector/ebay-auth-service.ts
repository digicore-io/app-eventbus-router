import { EventRequest, CompanyAppEvent, CompanyAppConfig, AppConfig } from "./classes";
import { EvendtDao } from "./dao/event-dao";
const AWS = require('aws-sdk');
AWS.config.update({ region: 'us-west-2' });
const axios = require('axios');
const eventDao = new EvendtDao();
const EBAY_URL: string = "https://api.sandbox.ebay.com/";

export class EbayAuthService {


    async getToken(eventRequest: EventRequest, config: CompanyAppConfig, appConfig: AppConfig) {
        try {
            let expires = new Date()
            expires.setTime(config.expires);
            console.log(expires);
            expires.setMinutes(expires.getMinutes() + 2)
            let currentUtc = new Date();

            console.log(currentUtc)

            if (currentUtc > expires) {
                console.log('RENEWING TOKEN')
                let authData = appConfig.appId + ":" + appConfig.certId;
                let buff = new Buffer(authData);
                let auth = buff.toString('base64');
                axios.defaults.headers.common['Authorization'] = "Basic " + auth;
                axios.defaults.headers.post['Content-Type'] = "application/x-www-form-urlencoded";

                let body = "grant_type=refresh_token&refresh_token=" + config.refreshToken + "&scope="
                for (let s of appConfig.scope)
                    body += s + " ";

                await axios.post(EBAY_URL + "identity/v1/oauth2/token", body).then(function (response) {
                    let expires = new Date();
                    expires.setHours(expires.getHours() + (response.data['expires_in'] / 60 / 60))
                    config.token = response.data['access_token'];
                    config.expires = expires.getTime();
                    //Saving token
                    eventDao.updateCompanyAppConfig(config)
                })
                    .catch(function (error) {
                        console.log(error);
                        throw error;
                    });
            }

            return config.token;
        } catch (err) {
            console.log(err)
        }
    }
}

