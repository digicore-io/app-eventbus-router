
export class BaseService{
    dbToCamelCaseList(results){
        for(var i = 0; i < results.length; i++)
            results[i] = this.dbToCamelCaseObject(results[i]);

        return results;    
    }

    dbToCamelCaseObject(result){
        let ro = {};

        Object.keys(result).forEach(function(key,index) {
            var nKey = key;
            nKey = key.toLocaleLowerCase();
            
            if(nKey.startsWith('vch_'))
                nKey = nKey.replace('vch_','');
            if(nKey.startsWith('n_'))    
                nKey = nKey.replace('n_','')
            if(nKey.startsWith('d_'))
                nKey  =  nKey.replace('d_','');    

            let sArray = nKey.split('');
            for(var i = 0; i < sArray.length; i++){
                if(sArray[i] == '_'){
                    sArray[i + 1] = sArray[i + 1].toUpperCase();
                }
            }
            nKey = sArray.join("");
            nKey = nKey.replace(/_/g, '');
            ro[nKey] = result[key];
        });

        return ro;
    }
}