import { HttpError, Route } from "@digicore-io/digicore-node-common-module/lib/common-classes";

export class Routes {
  public async handleGetRequest(event: any, route: Route) {
    switch (route.resource) {
      default: {
        console.log("Unknown Route Resource in GET request", JSON.stringify(route));
        throw new HttpError(404, "Unknown Route Resource in GET request: " + route.resource);
      }
    }
  }

  public async handlePostRequest(event: any, route: Route) {
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
