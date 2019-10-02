import { HttpError, Response } from "@DigiProMedia/digicore-node/common-classes";
import { BaseService } from "@DigiProMedia/digicore-node/base-service";
import { CompanyAppDao } from "../dao/company-app-dao";
const companyAppDao = new CompanyAppDao();

export class CompanyAppService extends BaseService {
  async getApplications(companyId: string, event: string) {
    if (!companyId) throw new HttpError(400, "Parameter companyId is required");

    if (!event) throw new HttpError(400, "Parameter event is required");

    let events: any = await companyAppDao.getCompanyAppEvents(companyId, event).catch(function(error) {
      console.log(error);
      throw error;
    });

    let apps = [];
    for (let event of events) {
      apps.push(await companyAppDao.getCompanyApp(event.applicationId, companyId));
    }

    return apps;
  }

  async saveApplicationCompany(applicationId: string, companyId: string, config: any) {
    if (!applicationId) throw new HttpError(400, "Parameter applicationId is required");

    await companyAppDao.saveCompanyApp(applicationId, companyId, config).catch(function(error) {
      console.log(error);
      throw error;
    });
  }
}
