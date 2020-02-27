package Service;

import Results.LoadResult;
import Requests.LoadRequest;

public class LoadService {
    /**
     * This function will take a load request object to fill
     * the server's databse and then return a Load Result object
     * of the function call.
     * @return the load result object
     */
    LoadResult event(LoadRequest r) {
        LoadResult newLoad = new LoadResult();
        return newLoad;
    }
}