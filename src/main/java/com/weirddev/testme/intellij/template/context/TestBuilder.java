package com.weirddev.testme.intellij.template.context;

import com.weirddev.testme.intellij.action.CreateTestMeAction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilder {
    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");
    public String stripGenerics(String canonicalName) {
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
    public String replaceType(String type,String replacementType){
        return String.format(replacementType, extractGenerics(type));
    }

    public String resolveTypeName(Type type,Map<String,String> replacementTypes){
        String canonicalName = type.getCanonicalName();
        String sanitizedCanonicalName = stripGenerics(canonicalName);
        if (replacementTypes!=null && replacementTypes.get(sanitizedCanonicalName)!=null) {
            String replacedSanitizedCanonicalName = replacementTypes.get(sanitizedCanonicalName);
            canonicalName = replaceType(canonicalName, replacedSanitizedCanonicalName);
        }
        return canonicalName;
    }
    //TODO consider passing around stringBuilder
    //TODO consider aggregating conf into context object and managing maps outside of template
    public String renderJavaCallParams(List<Param> params ,Map<String,String> replacementTypes,Map<String,String> defaultTypeValues, int recursionDepth){
        final StringBuilder stringBuilder = new StringBuilder();
        recursionDepth = recursionDepth + 1;
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(renderJavaCallParam(params.get(i).getType(), params.get(i).getName(),replacementTypes,defaultTypeValues, recursionDepth));
            }
        }
        return stringBuilder.toString();
    }

    public String renderJavaCallParam(Type type, String strValue,Map<String,String> replacementTypes,Map<String,String> defaultTypeValues, int recursionDepth){
        final StringBuilder stringBuilder = new StringBuilder();
        if (type.isArray()) {
            stringBuilder.append("new ").append(resolveTypeName(type, null)).append("[]{");
        }
        stringBuilder.append(renderJavaParam(type, strValue,replacementTypes,defaultTypeValues, recursionDepth));
        if (type.isArray()) {
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }
    private String renderJavaParam( Type type, String strValue,Map<String,String> replacementTypes,Map<String,String> defaultTypeValues, int recursionDepth) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            stringBuilder.append(defaultTypeValues.get(canonicalName));
        }
        else if(canonicalName.equals("java.lang.String")){
            stringBuilder.append("\"").append(strValue).append("\"");
        } else if (type.getEnumValues().size() > 0) {
            stringBuilder.append(resolveTypeName(type, null)).append(".").append(type.getEnumValues().get(0));
        } else {
            String typeName = resolveTypeName(type, replacementTypes);
                //todo isReplaced = type.canonicalName != typeName, if (!isReplaced) new; if (!isReplaced) typeName + "(%2$s);String.format(typeName,renderCtorParams(/*todo handle pairs*/extractGenerics($typeName).substring(1,n-1)),$type,$recursionDepth))"
            if (!typeName.startsWith("java.util.Arrays.")) {
                stringBuilder.append("new ");
            }
            stringBuilder.append(typeName).append("(");
            stringBuilder.append(renderCtorParams(type,typeName, replacementTypes,defaultTypeValues, recursionDepth));
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
    private String renderCtorParams( Type type, String typeName, Map<String,String> replacementTypes, Map<String,String> defaultTypeValues, int recursionDepth) {
        if (recursionDepth <= CreateTestMeAction.MAX_RECURSION_DEPTH && (typeName.equals(type.getName()) || typeName.equals(type.getCanonicalName()) && type.getConstructors().size() > 0)) {
            return renderJavaCallParams(type.getConstructors().get(0).getMethodParams(), replacementTypes, defaultTypeValues, recursionDepth);
        } else {
            return "";
        }
    }
}
