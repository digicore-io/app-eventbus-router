import { HttpError } from "@DigiProMedia/digicore-node/lib/common-classes";
import { BaseService } from "@DigiProMedia/digicore-node/lib/base-service";
import { AppEntityDao } from "../dao/app-entity-dao";
import { AppEntity } from "../interfaces";
const appEntityDao = new AppEntityDao();

export class AppEntityService extends BaseService {
  constructor() {
    super();
  }

  public async saveAppEntity(applicationId: string, companyId: string, entityId: string, appEntity: AppEntity) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!entityId) throw new HttpError(400, "Parameter entityId is required");

    return await appEntityDao.saveAppEntity(applicationId, companyId, entityId, appEntity).catch(function(error) {
      throw error;
    });
  }

  public async getAppEntity(applicationId: string, companyId: string, entityId: string) {
    //this.logToSlack("devops-event-bus", "test", "test");

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
