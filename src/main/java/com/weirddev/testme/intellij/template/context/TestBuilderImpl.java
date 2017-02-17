package com.weirddev.testme.intellij.template.context;

import com.weirddev.testme.intellij.action.CreateTestMeAction;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilderImpl implements TestBuilder {
    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");

    //TODO consider aggregating conf into context object and managing maps outside of template
    @Override
    public String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth){
        final StringBuilder stringBuilder = new StringBuilder();
        buildJavaCallParams(params, replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth){
        final StringBuilder stringBuilder = new StringBuilder();
        buildJavaCallParam(type, strValue, replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
        return stringBuilder.toString();
    }

    String resolveType(Type type, Map<String, String> replacementTypes){
        String canonicalName = type.getCanonicalName();
        String sanitizedCanonicalName = stripGenerics(canonicalName);
        if (replacementTypes!=null && replacementTypes.get(sanitizedCanonicalName)!=null) {
            String replacedSanitizedCanonicalName = replacementTypes.get(sanitizedCanonicalName);
            canonicalName = replaceType(canonicalName, replacedSanitizedCanonicalName);
        }
        return canonicalName;
    }

    String stripGenerics(String canonicalName) {
        return canonicalName.replaceFirst("<.*","");
    }
    String extractGenerics(String canonicalName) {
        Matcher matcher = GENERICS_PATTERN.matcher(canonicalName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
    private String replaceType(String type,String replacementType){
        return replacementType.replace("<TYPES>", extractGenerics(type));
    }

    private void buildJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        if (type.isArray()) {
            testBuilder.append("new ").append(type.getCanonicalName()).append("[]{");
        }
        buildJavaParam(type, strValue, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
        if (type.isArray()) {
            testBuilder.append("}");
        }
    }

    private void buildJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        recursionDepth = recursionDepth + 1;
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i != 0) {
                    testBuilder.append(", ");
                }
                buildJavaCallParam(params.get(i).getType(), params.get(i).getName(), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
            }
        }
    }

    private void buildJavaParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            testBuilder.append(defaultTypeValues.get(canonicalName));
        }
        else if(canonicalName.equals("java.lang.String")){
            testBuilder.append("\"").append(strValue).append("\"");
        } else if (type.getEnumValues().size() > 0) {
            testBuilder.append(canonicalName).append(".").append(type.getEnumValues().get(0));
        } else {
            String typeName = resolveType(type, replacementTypes);
            boolean isReplaced=false;
            if (!canonicalName.equals(typeName)) {
                isReplaced = true;
            }
            if (isReplaced) {
                //if (!isReplaced) typeName + "(%2$s);String.format(typeName,buildCtorParams(/*todo handle pairs*/extractGenerics($typeName).substring(1,n-1)),$type,$recursionDepth))"
                testBuilder.append(typeName);
            } else {
                testBuilder.append("new ");
                testBuilder.append(typeName).append("(");
                buildCtorParams(type, typeName, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
                testBuilder.append(")");
            }

        }
    }

    private void buildCtorParams(Type type, String typeName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        if (recursionDepth <= CreateTestMeAction.MAX_RECURSION_DEPTH && (typeName.equals(type.getName()) || typeName.equals(type.getCanonicalName()) && type.getConstructors().size() > 0)) {
            buildJavaCallParams(type.getConstructors().get(0).getMethodParams(), replacementTypes, defaultTypeValues, recursionDepth,testBuilder);
        }
    }
}
