import { Location, LocationRequest } from "../classes";
import { Response, HttpError } from "../../../Lambda-Common/node/common-classes";
import { BaseService } from "../../../Lambda-Common/node/base-service";
import { AppEntityDao } from "../dao/app-entity-dao";
import { AppEntity } from "../interfaces";
const appEntityDao = new AppEntityDao();

export class AppEntityService extends BaseService {
  async saveAppEntity(applicationId: string, companyId: string, entityId: string, appEntity: AppEntity) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!entityId) throw new HttpError(400, "Parameter entityId is required");

    return await appEntityDao.saveAppEntity(applicationId, companyId, entityId, appEntity).catch(function(error) {
      throw error;
    });
  }

  async getAppEntity(applicationId: string, companyId: string, entityId: string) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!entityId) throw new HttpError(400, "Parameter entityId is required");

    let appEntity: AppEntity = <AppEntity>await appEntityDao.getAppEntity(applicationId, companyId, entityId).catch(function(error) {
      console.log(error);
      throw error;
    });

    return appEntity;
  }
}
