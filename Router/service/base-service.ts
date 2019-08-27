import { Route, HttpError } from "../classes";


export abstract class BaseService {
  protected validateParam(route: Route, name:string){
    if(!route.queryParameters[name])
        throw new HttpError(400, name  + ' is required');
  }
}

