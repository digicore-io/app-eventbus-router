export class Error{
  status:number;
  message:string;
  constructor(status:number, message:string){
    this.status = status;
    this.message = message;
  }
}

export class Response{
  server:Server;
  status:number;
  success:boolean;
  payload:any;
  constructor(status:number, success:boolean, payload:any, event:any){
    this.server = new Server("api-digitrack", new Date().getTime(), event);
    this.status = status;
    this.success = success;
    this.payload = payload;
  }
}

export class Server{
  api:string;
  timestamp:number;
  endpoint:string;
  environment:string;
  constructor(api:string, timestamp:number, event:any){
    this.api = api;
    this.timestamp = timestamp;
    this.endpoint = event.path;
  }
}

export class Location {
  id: string;
  coordinates: Array<string>;
  created: number;
  userId: string;
  companyId: string;
  username:string;
}

export class LocationRequest {
  companyId: string;
  startTime:number;
  endTime:number;
}

export class Params{
  TableName:string;
  Item:any;
  IndexName:string;
  KeyConditionExpression:string;
  ExpressionAttributeNames:any;
  ExpressionAttributeValues:any;
  ScanIndexForward:boolean;
  Key:any;
}
export class Route {
  method: string;
  resource: string;
  pathParameters: any;
  queryParameters: any;

  constructor(method:string, resource:string, pathParameters:any, queryParameters:any){
    this.method = method;
    this.resource = resource;
    this.pathParameters = pathParameters;
    this.queryParameters = queryParameters;
  }
}
