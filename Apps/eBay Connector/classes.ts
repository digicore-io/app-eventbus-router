import { DH_UNABLE_TO_CHECK_GENERATOR } from "constants";

export class EventRequest {
    companyId: string;
    event: string;
    applicationId: string;
    payload: any;
    response: any;
    companyAppEventId: string;
}

export class CompanyAppEvent {
    id: string;
    companyId: string;
    event: string;
    applicationId: string;
    config: any;
}

export class AppConfig {
    returnProfileId: string;
    shippingProfileId: string;
    paymentProfileId: string;
    appId:string;
    devId:string;
    certId:string;
    scope:Array<string>;
}

export class CompanyAppConfig {
    applicationId:string;
    companyId:string;
    returnProfileId: string;
    shippingProfileId: string;
    paymentProfileId: string;
    token: string;
    refreshToken: string;
    expires: number;
    merchantLocationKey:string;
}

export class Response {
    server: Server;
    status: number;
    success: boolean;
    payload: any;
    constructor(status: number, success: boolean, payload: any, event: any) {
        this.server = new Server("app-ebay-connector", new Date().getTime(), event);
        this.status = status;
        this.success = success;
        this.payload = payload;
    }
}

export class Server {
    api: string;
    timestamp: number;
    endpoint: string;
    environment: string;
    constructor(api: string, timestamp: number, event: any) {
        this.api = api;
        this.timestamp = timestamp;
        this.endpoint = event.resource;
    }
}

export class Error {
    status: number;
    message: string;
    constructor(status: number, message: string) {
        this.status = status;
        this.message = message;
    }
}

export class Route {
    method: string;
    resource: string;
    pathParameters: any;
    queryParameters: any;

    constructor(method: string, resource: string, pathParameters: any, queryParameters: any) {
        this.method = method;
        this.resource = resource;
        this.pathParameters = pathParameters;
        this.queryParameters = queryParameters;
    }
}