import {Router} from './router';
import {Error, Response} from './classes';


export const handle = async (event, context:any) => {
  let router = new Router();
  let startTime = new Date().getTime();

  try{
    
    let result = await router.handleRoute(event);
    
    let endTime = new Date().getTime();

    return getResponse(new Response(200, true, result, event, endTime-startTime));
  }catch(err){
    let endTime = new Date().getTime();
    
    if(err instanceof Error == false)
      return getResponse(new Response(500, false, "" + err, null, endTime-startTime))    
    else
      return getResponse(new Response(err.status, false, err.message, event, endTime-startTime));
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
