package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;

public class CachedBeanDataToBean {


  public static void load(BeanDescriptor<?> desc, EntityBean bean, CachedBeanData cacheBeanData) {

    EntityBeanIntercept ebi = bean._ebean_getIntercept();

    BeanProperty idProperty = desc.getIdProperty();
    if (idProperty != null) {
      // load the id property
      loadProperty(bean, cacheBeanData, ebi, idProperty);
    }

    // load the non-many properties
    BeanProperty[] props = desc.propertiesNonMany();
    for (int i = 0; i < props.length; i++) {
      loadProperty(bean, cacheBeanData, ebi, props[i]);
    }

    BeanPropertyAssocMany<?>[] many = desc.propertiesMany();
    for (int i = 0; i < many.length; i++) {
      many[i].createReferenceIfNull(bean);
    }

    ebi.setLoadedLazy();
  }

  private static void loadProperty(EntityBean bean, CachedBeanData cacheBeanData, EntityBeanIntercept ebi, BeanProperty prop) {

    if (cacheBeanData.isLoaded(prop.getName())) {
      if (!ebi.isLoadedProperty(prop.getPropertyIndex())) {
        Object value = cacheBeanData.getData(prop.getName());
        prop.setCacheDataValue(bean, value);
      }
    }
  }

}