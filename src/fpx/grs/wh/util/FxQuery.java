/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package fpx.grs.wh.util;

import org.apache.commons.lang3.StringUtils;

import com.sun.javafx.stage.StageHelper;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * 根据选择器查找当前界面窗口元素节点工具类，类似 jquey 的 $('#id')
 * 使用实例：
 * <code>
        Node tabPane1 = FxQuery.$("#pageArea");
        TabPane tabPane2 = FxQuery.$("#pageArea", TabPane.class);

        Node tabPane3 = FxQuery.getById("pageArea");
        TabPane tabPane4 = FxQuery.getById("pageArea", TabPane.class);
        
        
 * </code>
 * @author niujh
 * @date 2016年11月10日
 */
@SuppressWarnings({ "restriction", "unchecked" })
public class FxQuery {
	
    /**
     * 根据选择器获得当前tab的节点
     * @param selector 选择器
     * @param t 节点类型class
     * @return
     */
    public static <T> T $(String selector, Class<T> t) {
        if (StringUtils.isEmpty(selector)) {
            return null;
        }
        TabPane tabPane = FxQuery.$$("#tabPane", TabPane.class);
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if(tab==null){
        	return (T) StageHelper.getStages().get(0).getScene().lookup(selector.trim());
        }else{
        	return (T)tab.getContent().lookup(selector.trim());
        }
    }
    
    /**
     * 根据选择器获得全局的节点
     * @param selector 选择器
     * @param t 节点类型class
     * @return
     */
    public static <T> T $$(String selector, Class<T> t) {
        if (StringUtils.isEmpty(selector)) {
            return null;
        }
        	return (T) StageHelper.getStages().get(0).getScene().lookup(selector.trim());
    }
    
    

    /**
     * 根据选择器获得节点
     * @param selector 选择器
     * @return
     */
    public static Node $(String selector) {
        return $(selector, Node.class);
    }
    
    


    /**
     * 根据id获得节点
     * @param selector 选择器
     * @return
     */
    public static Node getById(String idString) {
        if (StringUtils.isEmpty(idString)) {
            return null;
        }
        return getById(idString.trim(), Node.class);
    }

    /**
     * 根据id获得节点
     * @param selector 选择器
     * @param t 节点类型class
     * @return
     */
    public static <T> T getById(String idString, Class<T> t) {
        if (StringUtils.isEmpty(idString)) {
            return null;
        }
        return $("#" + idString.trim(), t);
    }

}
