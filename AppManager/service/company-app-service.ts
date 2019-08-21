import { Location, Response, LocationRequest, Error } from '../classes';

import { BaseService } from './BaseService';
import { AppEntityDao } from '../dao/app-entity-dao';
import { AppEntity } from '../interfaces';
import { CompanyAppDao } from '../dao/company-app-dao';
const companyAppDao = new CompanyAppDao();

export class CompanyAppService extends BaseService {

    

    async getApplications(companyId: string, event: string) {

        if(!companyId)
            throw new Error(400, "Parameter companyId is required");

        if (!event)
            throw new Error(400, "Parameter event is required");

        let events:any = await companyAppDao.getCompanyAppEvents(companyId, event)
            .catch(function (error) {
                console.log(error)
                throw error;
            });
        
        let apps = [];
        for(let event of events){
            apps.push(await companyAppDao.getCompanyApp(event.applicationId, companyId));
        }
        
        return apps;
    }


    async saveApplicationCompany(applicationId: string, companyId:string, config:any) {

        if(!applicationId)
            throw new Error(400, "Parameter applicationId is required");

        

        await companyAppDao.saveCompanyApp(applicationId, companyId, config)
            .catch(function (error) {
                console.log(error)
                throw error;
            });

    }
}