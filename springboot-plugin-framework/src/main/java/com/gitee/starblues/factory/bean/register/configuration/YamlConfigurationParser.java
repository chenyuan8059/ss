package com.gitee.starblues.factory.bean.register.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.gitee.starblues.exception.ConfigurationParseException;
import com.gitee.starblues.integration.IntegrationConfiguration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * yaml 配置解析者
 * @author zhangzhuo
 * @version 1.0
 */
public class YamlConfigurationParser extends AbstractConfigurationParser{

    private final YAMLFactory yamlFactory;
    private final ObjectMapper objectMapper;

    public YamlConfigurationParser(IntegrationConfiguration configuration) {
        super(configuration);
        this.yamlFactory = new YAMLFactory();
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }


    @Override
    protected Object parse(Resource resource, Class<?> pluginConfigClass)
            throws ConfigurationParseException{
        try {
            InputStream input = new FileInputStream(resource.getFile());
            YAMLParser yamlParser = yamlFactory.createParser(input);
            final JsonNode node = objectMapper.readTree(yamlParser);
            if(node == null){
                return pluginConfigClass.newInstance();
            }
            TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
            final Object o = objectMapper.readValue(treeTraversingParser, pluginConfigClass);
            return o;
        } catch (Exception e){
            throw new ConfigurationParseException("Yaml Parse Failure : " + e.getMessage(), e);
        }
    }
}
