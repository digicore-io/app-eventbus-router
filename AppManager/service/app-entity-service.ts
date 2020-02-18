import { HttpError } from "@digicore-io/digicore-node-common-module/lib/common-classes";
import { BaseService } from "@digicore-io/digicore-node-common-module/lib/base-service";
import { AppEntityDao } from "../dao/app-entity-dao";
import { AppEntity } from "../interfaces";
const appEntityDao = new AppEntityDao();

export class AppEntityService extends BaseService {
  constructor() {
    super();
  }

  public async saveAppEntity(applicationId: string, companyId: string, digicoreEntityId: string, appEntity: AppEntity) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!digicoreEntityId) throw new HttpError(400, "Parameter internalEntityId is required");

    return await appEntityDao.saveAppEntity(applicationId, companyId, digicoreEntityId, appEntity).catch(function(error) {
      throw error;
    });
  }

  public async getAppEntity(applicationId: string, companyId: string, digicoreEntityId: string) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!digicoreEntityId) throw new HttpError(400, "Parameter internalEntityId is required");

    let appEntity: AppEntity = <AppEntity>await appEntityDao.getAppEntity(applicationId, companyId, digicoreEntityId).catch(function(error) {
      console.log(error);
      throw error;
    });

    return appEntity;
  }

  public async getAppEntityByExternalId(applicationId: string, companyId: string, externalEntityId: string) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!externalEntityId) throw new HttpError(400, "Parameter externalEntityId is required");

    let appEntity: AppEntity = <AppEntity>await appEntityDao.getAppEntityByExternalId(applicationId, companyId, externalEntityId).catch(function(error) {
      console.log(error);
      throw error;
    });

    return appEntity;
  }
}
