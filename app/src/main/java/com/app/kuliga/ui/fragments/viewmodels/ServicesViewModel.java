package com.app.kuliga.ui.fragments.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.app.kuliga.data.api.retrofit.ApiCall;
import com.app.kuliga.data.entity.Service;

import java.util.ArrayList;
import java.util.List;

public class ServicesViewModel extends ViewModel {

    private static List<Service> services = new ArrayList<>();
    private static MutableLiveData<LoadServicesResult> loadServicesResult = new MutableLiveData<>();
    private static MutableLiveData<LoadServiceResult> loadServiceResult = new MutableLiveData<>();

    public static LiveData<LoadServicesResult> getServicesResult() {
        return loadServicesResult;
    }
    public static LiveData<LoadServiceResult> getServiceResult() {
        return loadServiceResult;
    }

    public static void loadServices(){
        ApiCall.getInstance().getServices(loadServicesResult);
    }

    public static void loadServicesFromDB(){
        List<Service> serv = Service.listAll(Service.class);
        loadServicesResult.setValue(new LoadServicesResult(serv));
    }

    public static void saveServicesToDB(List<Service> servic){
        deleteServicesFromDB();
        for (Service service : servic) {
            service.save();
        }
        List<Service> cs = Service.listAll(Service.class);
        if (cs != null) {
            setServices(cs);
        }
    }

    public static void saveServiceToDB(Service service){
        Service newServ = service;
        List<Service> servicesDB = Service.listAll(Service.class);
        if (servicesDB != null && !servicesDB.isEmpty()) {
            for (Service oldServ : servicesDB) {
                if (service.getServiceId().equals(oldServ.getServiceId())){
                    newServ = oldServ;
                    newServ.setName(service.getName());
                    newServ.setPrice(service.getPrice());
                    newServ.setTariffTime(service.getTariffTime());
                    newServ.setTimeWork(service.getTimeWork());
                    break;
                }
            }
            newServ.save();
            List<Service> cs = Service.listAll(Service.class);
            if (cs != null) {
                setServices(cs);
            }
        }
    }

    private static boolean isServiceFromDB(Service service){
        List<Service> services = Service.listAll(Service.class);
        if (services != null && !services.isEmpty()){
            return false;
        }
        for (Service oldService : services) {
            if (service.getServiceId().equals(oldService.getServiceId())){
                return true;
            }
        }
        return false;
    }

    public static void deleteServicesFromDB(){
        List<Service> services = Service.listAll(Service.class);
        if (services != null && !services.isEmpty()) {
            for (Service service : services) {
                service.delete();
            }
        }
    }

    public static void loadTariff(Service service){
        ApiCall.getInstance().getServiceTariff(loadServiceResult, service.getServiceId());
    }

    public static List<Service> getServices() {
        return services;
    }

    public static void setServices(List<Service> services) {
        ServicesViewModel.services = services;
    }


    public static class LoadServicesResult {
        @Nullable
        private List<Service> success;
        @Nullable
        private String error;

        public LoadServicesResult(@Nullable String error) {
            this.error = error;
        }

        public LoadServicesResult(@Nullable List<Service> success) {
            this.success = success;
        }

        @Nullable
        public List<Service> getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }

    public static class LoadServiceResult {
        @Nullable
        private Service success;
        @Nullable
        private String error;

        public LoadServiceResult(@Nullable String error) {
            this.error = error;
        }

        public LoadServiceResult(@Nullable Service success) {
            this.success = success;
        }

        @Nullable
        public Service getSuccess() {
            return success;
        }

        @Nullable
        public String getError() {
            return error;
        }
    }
}