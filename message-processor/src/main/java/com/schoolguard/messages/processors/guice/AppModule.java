package com.schoolguard.messages.processors.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.schoolguard.core.ws.client.*;
import com.schoolguard.message.storage.client.PersistenceAPI;
import com.schoolguard.messages.processors.bolts.*;
import com.schoolguard.messages.producers.KafkaMsgProducer2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Rogers on 15-5-14.
 */
public class AppModule extends AbstractModule {

    @Override
    public void configure(){
        Names.bindProperties(binder(), loadProperties("app.properties"));

        bind(DuplicateFilterBolt.class);

        bind(NoticeSplitterBolt.class);
        bind(AssignmentSplitterBolt.class);
        bind(ExamSplitterBolt.class);
        bind(OASplitterBolt.class);
        bind(AttendanceSplitterBolt.class);

        bind(PermissionCheckBolt.class);

        bind(SendWeixinBolt.class);
        bind(SendSMSBolt.class);

        bind(PersistenceBolt.class);

        bind(MsgSortOutBolt.class);

        bind(SendCommonSMSBolt.class);

    }

    @Provides
    private KafkaMsgProducer2 kafkaMsgProducer2(@Named("kafka.servers") String bootstrapServers){
        return new KafkaMsgProducer2(bootstrapServers);
    }


    @Provides
    @Named("ApiRootUrl")
    private String apiRootUrl(@Named("coreData.rootUrl") String rootUrl){
       return rootUrl;
    }

    @Provides
    @Named("ApiUsername")
    private String getUsername(@Named("coreData.httpAuth.username") String username){
        return username;
    }

    @Provides
    @Named("ApiPassword")
    private String getPassword(@Named("coreData.httpAuth.password") String password){
        return password;
    }

    @Provides
    public SchoolApi getSchoolApi(@Named("ApiRootUrl") String apiRootUrl,
                                  @Named("ApiUsername") String username,
                                  @Named("ApiPassword") String password){
        SchoolApi schoolApi = new SchoolApi(apiRootUrl, username, password);
        return schoolApi;
    }

    @Provides
    public SchoolSectionAPI getSchoolSectionApi(@Named("ApiRootUrl") String apiRootUrl,
                                  @Named("ApiUsername") String username,
                                  @Named("ApiPassword") String password){
        return new SchoolSectionAPI(apiRootUrl, username, password);
    }

    @Provides
    public SchoolGradeAPI getSchoolGradeApi(@Named("ApiRootUrl") String apiRootUrl,
                                            @Named("ApiUsername") String username,
                                            @Named("ApiPassword") String password){
        return new SchoolGradeAPI(apiRootUrl, username, password);
    }

    @Provides
    public SchoolClassAPI getSchoolClassApi(@Named("ApiRootUrl") String apiRootUrl,
                                            @Named("ApiUsername") String username,
                                            @Named("ApiPassword") String password){
        return new SchoolClassAPI(apiRootUrl, username, password);
    }

    @Provides
    public TeacherAPI getTeacherApi(@Named("ApiRootUrl") String apiRootUrl,
                                    @Named("ApiUsername") String username,
                                    @Named("ApiPassword") String password){
        return new TeacherAPI(apiRootUrl, username, password);
    }

    @Provides
    public StudentAPI getStudentApi(@Named("ApiRootUrl") String apiRootUrl,
                                    @Named("ApiUsername") String username,
                                    @Named("ApiPassword") String password){
        return new StudentAPI(apiRootUrl, username, password);
    }

    @Provides
    public GuardianApi getGuardianApi(@Named("ApiRootUrl") String apiRootUrl,
                                    @Named("ApiUsername") String username,
                                    @Named("ApiPassword") String password){
        return new GuardianApi(apiRootUrl, username, password);
    }

    @Provides
    public AccessTokenAPI getAccessTokenApi(@Named("ApiRootUrl") String apiRootUrl,
                                            @Named("ApiUsername") String username,
                                            @Named("ApiPassword") String password){
        return new AccessTokenAPI(apiRootUrl, username, password);
    }

    @Provides
    @Named("stormNumberOfWorkers")
    public int getStormNumberOfWorkers(@Named("storm.workers") int workers){
        return workers;
    }

    @Provides
    public PersistenceAPI persistenceAPI(@Named("elasticsearch.server") String serverUrl){
        return new PersistenceAPI(serverUrl);
    }

    private Properties loadProperties(String propertiesFile){
        Properties configProperties = new Properties();

        try{
            configProperties.load(AppModule.class.getClassLoader().getResourceAsStream(propertiesFile));
        }catch (FileNotFoundException exception){
            throw new RuntimeException("Load properties failed: " + exception.getMessage());

        } catch (IOException exception){
            throw new RuntimeException("Load properties failed: " + exception.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return configProperties;
    }

}
