package offender.backend.com.example.Offender.service;

import offender.backend.com.example.Offender.entities.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

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
}
