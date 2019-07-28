import {Router} from './router';
import {Error, Response} from './classes';
import { isRegExp } from 'util';

export const handle = async (event, context:any) => {
  let router = new Router();
  
  try{
    let result = await router.handleRoute(event);

    console.log('result', result)
    return getResponse(new Response(200, true, result, event));
  }catch(err){
    if(err instanceof Error == false)
      return getResponse(new Response(500, false, "" + err, null))    
    else
      return getResponse(new Response(err.status, false, err.message, event));
  }
};

function getResponse(body: any){
  return {
    statusCode: body.status,
    headers: {
      "Access-Control-Allow-Origin" : "*", 
      "Access-Control-Allow-Credentials" : true 
    },
    body: JSON.stringify(body),
  };
}
