import {Location, Response, LocationRequest, Error} from '../classes';

import {BaseService} from './BaseService';
import { AppEntityDao } from '../dao/app-entity-dao';
import { AppEntity } from '../interfaces';
const appEntityDao = new AppEntityDao();

export class AppEntityService extends BaseService{
        
    async saveAppEntity(appEntity:AppEntity) {
        if(!appEntity.entityId)
            throw new Error(400, "Parameter entityId is required");

        if(!appEntity.companyId)
            throw new Error(400, "Parameter companyId is required");

        await appEntityDao.saveAppEntity(appEntity)
        .catch(function(error) {
            throw error;
        });

        return location;
    }

    async getAppEntity(entityId:string, companyId:string){
        if(!entityId)
            throw new Error(400, "Parameter entityId is required");

        if(!companyId)
            throw new Error(400, "Parameter companyId is required");

        let appEntity:AppEntity = <AppEntity> await appEntityDao.getAppEntity(entityId,companyId)
        .catch(function(error) {
            console.log(error)
            throw error;
        });

        return appEntity;
    }
}