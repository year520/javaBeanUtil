package com.util;

import com.bean.diff.DiffVO;
import com.bean.diff.DiffVO.ModType;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JxpathCommonUtil {

	private Properties properties;

	private static final String PATH_SPILT = "/";
	/**
	 * do not need replace
	 */
	private static final int FLAG_NO_NEED = 0;
	/**
	 * list replace
	 */
	private static final int FLAG_NEED_LIST = 1;
	/**
	 * normal replace
	 */
	private static final int FLAG_NEED_NORMAL = 2;
	
	private static final String DEFAULT_PROPERTIES = "config/key.properties";

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setProperties(String file) throws IOException {
		Properties properties = new Properties();
		InputStream is = new FileInputStream(file);
		properties.load(is);
		this.properties = properties;

	}

	/**
	 * BUILD [KEYNAME='KEYValue']
	 * 
	 * @param keyName
	 * @param keyValue
	 * @return string
	 */
	private String buildKey(String keyName, Object keyValue) {
		return "[" + keyName + "='" + keyValue + "']";
	}

	/**
	 * Add two list and remove the duplicate data
	 * 
	 * @param lst1
	 * @param lst2
	 * @return totalList
	 */
	private List<JxpathCommonBean> addList(List<JxpathCommonBean> lst1, List<JxpathCommonBean> lst2) {
		List<JxpathCommonBean> all = new ArrayList<JxpathCommonBean>();
		all.addAll(lst1);
		all.addAll(lst2);
		// remove the duplicate data
		Map<String, JxpathCommonBean> map = new HashMap<String, JxpathCommonBean>();
		for (JxpathCommonBean bean : all) {
			map.put(bean.toString(), bean);
		}
		all.clear();
		for (String key : map.keySet()) {
			all.add(map.get(key));
		}
		// sort
		Collections.sort(all, new JxpathCommonBean().new ComparatorBean());
		return all;
	}

	/**
	 * sort the list
	 * 
	 * @param list
	 * @return
	 */
	public List<JxpathCommonBean> sort(List<JxpathCommonBean> list) {
		Collections.sort(list, new JxpathCommonBean().new ComparatorBean());
		return list;
	}

	/**
	 * ADD KEY TO PATH
	 * 
	 * @param allPath
	 * @param allKeys
	 * @return List
	 */
	public List<JxpathCommonBean> addKeyToPath(List<JxpathCommonBean> allPath, List<JxpathCommonBean> allKeys) {
		List<JxpathCommonBean> paths = new ArrayList<JxpathCommonBean>();
		for (JxpathCommonBean path : allPath) {
			int flag = this.needReplaceKey(path, allKeys);
			switch (flag) {
			case FLAG_NO_NEED:
				paths.add(path);
			case FLAG_NEED_LIST:
				for (JxpathCommonBean key : allKeys) {
					if (key.getParentPath().equals(path.getJxpath()))
						paths.add(this.buildJxpathCommonBean(path.getLevel(), key.getJxpath(), path.getParentPath(), path.isList(), path.getKeyName()));
				}
			case FLAG_NEED_NORMAL:
				for (JxpathCommonBean key : allKeys) {
					if (key.getParentPath().equals(path.getParentPath()))
						paths.add(this.buildJxpathCommonBean(path.getLevel(), path.getJxpath().replaceFirst(key.getParentPath(), key.getParentPath()), path.getParentPath(), path.isList(),
								path.getKeyName()));
				}
				;
				break;
			}
		}
		return paths;
	}

	/**
	 * check the list
	 * 
	 * @param bean
	 * @param keyList
	 * @return
	 */
	private int needReplaceKey(JxpathCommonBean bean, List<JxpathCommonBean> keyList) {
		int flag = FLAG_NO_NEED;
		for (JxpathCommonBean key : keyList) {
			if (null == bean.getParentPath()) {
				return FLAG_NO_NEED;
			} else if (bean.isList()) {
				return FLAG_NEED_LIST;
			} else if (!bean.isList() && bean.getParentPath().equals(key.getParentPath())) {
				return FLAG_NEED_NORMAL;
			}
		}
		return flag;

	}

	/**
	 * compare two bean
	 * 
	 * @param before
	 * @param after
	 * @return
	 * @throws IOException 
	 */
	public List<DiffVO> compareBean(Object before, Object after) throws IOException {
		List<JxpathCommonBean> o1path = getAllXpath(before);
		List<JxpathCommonBean> o2path = getAllXpath(after);
		List<JxpathCommonBean> allPath = addList(o1path, o2path);
		return this.compareBean(allPath, before, after);
	}

	/**
	 * get Value From Xpath
	 * 
	 * @param jxpath
	 * @param path
	 * @return object
	 */
	public Object getValueFromXpath(JXPathContext jxpath, String path) {
		try {
			return jxpath.getValue(path);
		} catch (JXPathNotFoundException e) {
			return null;
		}
	}

	/**
	 * get Value From Xpath
	 * 
	 * @param allPath
	 * @param jxpath1
	 * @param jxpath2
	 * @return
	 */
	private List<DiffVO> compareBean(List<JxpathCommonBean> allPath, Object beforeObject, Object afterObject) {
		JXPathContext jxpath1 = JXPathContext.newContext(beforeObject);
		JXPathContext jxpath2 = JXPathContext.newContext(afterObject);
		List<DiffVO> diffVOList = new ArrayList<DiffVO>();
		int id = 1;
		Map<String, String> map = new HashMap<String, String>();
		for (JxpathCommonBean path : allPath) {
			Object o1 = this.getValueFromXpath(jxpath1, path.getJxpath());
			Object o2 = this.getValueFromXpath(jxpath2, path.getJxpath());
			ModType modType = this.getModType(o1, o2, path.isList());
			if (null != modType) {
				boolean flag = true;
				DiffVO vo = this.buildDiffVO(String.valueOf(id), String.valueOf(o1), String.valueOf(o2), modType, path.getJxpath());
				for (String key : map.keySet()) {
					if (null != path.getParentPath() && path.getParentPath().startsWith(key)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					diffVOList.add(vo);
					id++;
					map.put(path.getJxpath(), StringUtils.EMPTY);
				}

			}

		}
		return diffVOList;
	}

	/**
	 * get ModType
	 * 
	 * @param o1
	 * @param o2
	 * @param isList
	 * @return ModType
	 */
	private ModType getModType(Object o1, Object o2, boolean isList) {
		ModType modType = null;
		if (isList) {
			if (null != o1 && null == o2) {
				modType = ModType.Delete;
			} else if (null == o1 && null != o2) {
				modType = ModType.Add;
			}

		} else {
			if (StringUtils.isNotEmpty(ObjectUtils.toString(o1)) && StringUtils.isEmpty(ObjectUtils.toString(o2))) {
				modType = ModType.Delete;
			} else if (StringUtils.isEmpty(ObjectUtils.toString(o1)) && StringUtils.isNotEmpty(ObjectUtils.toString(o2))) {
				modType = ModType.Add;
			} else if (!String.valueOf(o1).equals(String.valueOf(o2))) {
				modType = ModType.Modify;
			}
		}
		return modType;
	}

	/**
	 * BUILD DiffVO
	 * 
	 * @param id
	 * @param beforeValue
	 * @param afterValue
	 * @param modType
	 * @param jxpath
	 * @return DiffVO
	 */
	private DiffVO buildDiffVO(String id, String beforeValue, String afterValue, ModType modType, String jxpath) {
		DiffVO vo = new DiffVO();
		vo.setAfterValue(afterValue);
		vo.setBeforeValue(beforeValue);
		vo.setId(id);
		vo.setModType(modType);
		vo.setJxPath(jxpath);
		return vo;
	}

	/**
	 * get all Jxpath
	 * 
	 * @param list
	 * @param object
	 * @param level
	 * @param parentFieldName
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<JxpathCommonBean> getAllXpath(List<JxpathCommonBean> list, Object o, int level, String parentFieldName) throws IOException {
		//if properties is null,set the default properties
		if(null==properties){
			this.setProperties(DEFAULT_PROPERTIES);
		}
		//get the object class
		Class<?> clz = o.getClass();
		//init the object to jxpathContext
		JXPathContext jxpath = JXPathContext.newContext(o);
		//get all the fields in the object class
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Class<?> fieldType = field.getType();
			if (isSimpleType(fieldType)) {//if is simpleType, add the path to list
				list.add(this.buildJxpathCommonBean(level, addPath(parentFieldName, fieldName), parentFieldName, false, null));
			} else if (isMap(fieldType)) {//if is map,add the path to list
				Map map = (Map) jxpath.getValue(fieldName);
				for (Object key : map.keySet()) {
					list.add(this.buildJxpathCommonBean(level, addPath(parentFieldName, fieldName) + PATH_SPILT + key, parentFieldName, false, null));
				}
			} else if (isList(fieldType)) {//if is list,need call this method again
				List lst = (List) jxpath.getValue(fieldName);
				if (null != lst && lst.size() > 0) {
					String key = (String) properties.get(lst.get(0).getClass().getName());
					Iterator it = jxpath.iterate(fieldName);
					while (it.hasNext()) {
						Object object = it.next();
						JXPathContext keyContext = JXPathContext.newContext(object);
						Object keyValue = keyContext.getValue(key);
						String listPath = addPath(parentFieldName, fieldName) + buildKey(key, keyValue);
						list.add(this.buildJxpathCommonBean(level, listPath, parentFieldName, true, key));
						getAllXpath(list, object, level + 1, listPath);
					}
				}

			} else {
				//else type ,such as java bean
				getAllXpath(list, jxpath.getValue(fieldName), level + 1, addPath(parentFieldName, fieldName));
			}

		}

		return list;
	}

	/**
	 * get all jxpath from Object
	 * 
	 * @param source
	 * @return all path list
	 * @throws IOException 
	 */
	public List<JxpathCommonBean> getAllXpath(Object source) throws IOException {
		List<JxpathCommonBean> list = new ArrayList<JxpathCommonBean>();
		this.getAllXpath(list, source, 1, null);
		// Collections.sort(list, new JxpathCommonBean().new ComparatorBean());
		return list;
	}

	/**
	 * add parent path to the child
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	private String addPath(String parent, String child) {
		return null != parent ? (parent + PATH_SPILT + child) : child;
	}

	/**
	 * build JxpathCommonBean
	 * 
	 * @param level
	 * @param jxpath
	 * @param parentPath
	 * @param isList
	 * @param keyName
	 * @return bean
	 */
	private JxpathCommonBean buildJxpathCommonBean(int level, String jxpath, String parentPath, boolean isList, String keyName) {
		JxpathCommonBean jxpathCommonBean = new JxpathCommonBean();
		jxpathCommonBean.setJxpath(jxpath);
		jxpathCommonBean.setLevel(level);
		jxpathCommonBean.setParentPath(parentPath);
		jxpathCommonBean.setList(isList);
		jxpathCommonBean.setKeyName(keyName);
		return jxpathCommonBean;
	}

	/**
	 * is isSimpleType
	 * 
	 * @param z
	 * @return
	 */
	private boolean isSimpleType(Class<?> z) {
		if (null != z
				&& (z.equals(int.class) || z.equals(short.class) || z.equals(long.class) || z.equals(float.class) || z.equals(double.class) || z.equals(boolean.class) || z.equals(char.class)
						|| z.equals(String.class) || z.equals(Integer.class) || z.equals(Short.class) || z.equals(Long.class) || z.equals(Float.class) || z.equals(Double.class)
						|| z.equals(Boolean.class) || z.equals(BigDecimal.class) || z.equals(Date.class) || z.equals(java.sql.Date.class) || z.equals(Character.class) || z.equals(StringBuffer.class) || z
						.equals(StringBuilder.class))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * IS MAP
	 * 
	 * @param z
	 * @return
	 */
	private boolean isMap(Class<?> z) {
		if (null != z && (z.equals(Map.class) || z.equals(HashMap.class) || z.equals(LinkedHashMap.class))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * is list
	 * 
	 * @param z
	 * @return
	 */
	private boolean isList(Class<?> z) {
		if (null != z && (z.equals(List.class) || z.equals(ArrayList.class) || z.equals(LinkedList.class))) {
			return true;
		} else {
			return false;
		}
	}

}
