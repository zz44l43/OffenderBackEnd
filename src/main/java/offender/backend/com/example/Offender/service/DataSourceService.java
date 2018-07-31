package offender.backend.com.example.Offender.service;

import offender.backend.com.example.Offender.entities.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class DataSourceService {
    public Data getPeopleSource(File file) {
        Serializer serializer = new Persister();
        Data data = null;

        try {
            data = serializer.read(Data.class, file);

        } catch (Exception e) {

        } finally {
            return data;
        }
    }

    public ArrayList<Attributes> getUniqueKeyAttributes(Data data){
        ArrayList<Attributes> attributesList = new ArrayList<Attributes>();
        ArrayList<String> uniqueKeys = new ArrayList<String>();
        for (Entities entity: data.getEntitiesList()
                ) {
            for (Attributes attributes: entity.getList()
                    ) {
                if(!uniqueKeys.contains(attributes.getAttributeKey())){
                    uniqueKeys.add(attributes.getAttributeKey());
                    attributesList.add(attributes);
                }
            }
        }
        return attributesList;
    }

    public ArrayList<AttributesVM> getUniqueKeyAttributesVM(Data data, User creator){
        ArrayList<Attributes> attributesList = getUniqueKeyAttributes(data);
        ArrayList<AttributesVM> attributesVMList = new ArrayList<AttributesVM>();
        for (Attributes attribute: attributesList
             ) {
            AttributesVM attributesVM = new AttributesVM();
            attributesVM.setAttributeKey(attribute.getAttributeKey());
            attributesVM.setCreator(creator);
            attributesVM.setType(attribute.getType());
            attributesVM.setDoubleValue(attribute.getDoubleValue());
            attributesVM.setListKey(attribute.getListKey());
            attributesVM.setStringValue(attribute.getStringValue());
            attributesVMList.add(attributesVM);

        }

        return attributesVMList;
    }

    public Data getFilteredPeopleSource(File file,  ArrayList<AttributesVM> attributesVMList){
        Data data = getPeopleSource(file);
        ArrayList<AttributesVM> newAttributesVmList = new ArrayList<AttributesVM>();
        if(data != null){
            newAttributesVmList.addAll(attributesVMList.stream().filter(x -> !x.getSelected()).collect(Collectors.toList()));
            for (AttributesVM attributeVm: newAttributesVmList
                 ) {
                for (Entities entity: data.getEntitiesList()
                     ) {
                     entity.getList().removeIf(x -> x.getAttributeKey().equals(attributeVm.getAttributeKey()));
                }
            }
        }
        return data;
    }
    public File createXML(Data data, String path){
        Serializer serializer = new Persister();
        File result = new File("filtered.xml");
        try {
            serializer.write(data, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Data mergeEntitiesAndRecords(Data data, ArrayList<IntelRecord> records){
        for (Entities entity: data.getEntitiesList()
             ) {
            ArrayList<Locations> locations = entity.getLocations();
            if(locations == null || locations.size() == 0){
                continue;
            }
            for (Locations location: locations
                 ) {
                for (IntelRecord record: records
                     ) {
                    if(location.getId().equals(record.getLocations().getId())){
                        if(entity.getRecordLocations() == null){
                            entity.setRecordLocations(new ArrayList<IntelRecord>());
                        }
                        entity.getRecordLocations().add(record);
                    }
                }
            }
        }
        return data;
    }

    public IntelAttributes getLocationAttribute(IntelRecord record){
        if(record.getLocations() == null ) return null;
        for (IntelAttributes attribute: record.getIntelAttributes()
             ) {
            if(attribute.getRecordAttribute().getName().toLowerCase().contains("location")){
                return attribute;
            }
        }
        return null;
    }
}
