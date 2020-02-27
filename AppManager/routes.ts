import { AppEntityService } from "./service/app-entity-service";
import { Route, HttpError } from "@digicore-io/digicore-node-common-module/lib/common-classes";
import { AppEntity } from "./interfaces";
import { CompanyAppService } from "./service/company-app-service";
const appEntityService = new AppEntityService();
const companyAppService = new CompanyAppService();

export class Routes {
  public async handleGetRequest(event: any, route: Route) {
    switch (route.resource) {
      case "/applications/{applicationId}/companies/{companyId}/entities/{digicoreEntityId}": {
        return await appEntityService.getAppEntity(route.pathParameters.applicationId, route.pathParameters.companyId, route.pathParameters.digicoreEntityId);
      }
      case "/applications/{applicationId}/companies/{companyId}/entities/external/{externalEntityId}": {
        return appEntityService.getAppEntityByExternalId(route.pathParameters.applicationId, route.pathParameters.companyId, route.pathParameters.externalEntityId);
      }
      case "/companies/{companyId}/applications": {
        return companyAppService.getApplications(route.pathParameters.companyId, route.queryParameters.event);
      }
      default: {
        console.log("Unknown Route Resource in GET request", JSON.stringify(route));
        throw new HttpError(404, "Unknown Route Resource in GET request: " + route.resource);
      }
    }
  }

  public async handlePostRequest(event: any, route: Route) {
    let jsonObject: any = JSON.parse(event.body);
    switch (route.resource) {
      default: {
        console.log("Unknown Route Resource in POST Request", JSON.stringify(route));
        throw new HttpError(404, "Unknown Route Resource in POST request: " + route.resource);
      }
    }
  }

  public async handlePutRequest(event: any, route: Route) {
    let jsonObject: any = JSON.parse(event.body);
    switch (route.resource) {
      case "/applications/{applicationId}/companies/{companyId}/entities/{digicoreEntityId}": {
        await appEntityService.saveAppEntity(route.pathParameters.applicationId, route.pathParameters.companyId, route.pathParameters.digicoreEntityId, <AppEntity>jsonObject);
        break;
      }
      case "/applications/{applicationId}/companies/{companyId}": {
        await companyAppService.saveApplicationCompany(route.pathParameters.applicationId, route.pathParameters.companyId, jsonObject);
        break;
      }
      default: {
        console.log("Unknown Route Resource in PUT Request", JSON.stringify(route));
        throw new HttpError(404, "Unknown Route Resource in PUT request: " + route.resource);
      }
    }
  }

  public async handleDeleteRequest(event: any, route: Route) {
    switch (route.resource) {
      default: {
        console.log("Unknown Route Resource in DELETE Request", JSON.stringify(route));
        throw new HttpError(404, "Unknown Route Resource in DELETE request: " + route.resource);
      }
    }
  }
}