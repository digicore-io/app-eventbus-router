import { Location, Response, LocationRequest, Error } from '../classes';

import { BaseService } from './BaseService';
import { AppEntityDao } from '../dao/app-entity-dao';
import { AppEntity } from '../interfaces';
const appEntityDao = new AppEntityDao();

export class AppEntityService extends BaseService {

    async saveAppEntity(appId: string, companyId: string, entityId: string,appEntity: AppEntity) {
        if (!appId)
            throw new Error(400, "Parameter entityId is required");

        if (!companyId)
            throw new Error(400, "Parameter companyId is required");

        if (!entityId)
            throw new Error(400, "Parameter entityId is required");

        await appEntityDao.saveAppEntity(appId, companyId, entityId, appEntity)
            .catch(function (error) {
                throw error;
            });
    }

    async getAppEntity(appId: string, companyId: string, entityId: string) {

        if(!appId)
            throw new Error(400, "Parameter appId is required");

        if (!companyId)
            throw new Error(400, "Parameter companyId is required");

        if (!entityId)
            throw new Error(400, "Parameter entityId is required");

        let appEntity: AppEntity = <AppEntity>await appEntityDao.getAppEntity(appId, companyId, entityId)
            .catch(function (error) {
                console.log(error)
                throw error;
            });

        return appEntity;
    }
}