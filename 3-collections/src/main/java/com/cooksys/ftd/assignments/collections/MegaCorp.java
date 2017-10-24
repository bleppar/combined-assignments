package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import com.cooksys.ftd.assignments.collections.model.WageSlave;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

	HashMap<FatCat, Set<Capitalist>> directory = new HashMap();
	
    /**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent itself,
     * do not add it and return false
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
    @Override
    public boolean add(Capitalist capitalist) {
       
    	Capitalist person = capitalist;
        Set<Capitalist> parentChildren;
        Set<Capitalist> children;
        
        if( person == null || !(person instanceof Capitalist) || has(person)){
        	return false;
        }
        if (person instanceof WageSlave && !person.hasParent()){
        	return false;
        }
        if(person.hasParent()){
        	add (person.getParent());
        	parentChildren = directory.get(person.getParent());
        	parentChildren.add(person);
        	directory.put(person.getParent(), parentChildren);
        }
        if(person instanceof FatCat){
        	children =new HashSet<Capitalist>();
        	directory.put((FatCat) person, children);
 
        }
	return true;
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
        
    	if (capitalist == null || directory.isEmpty()){
        	return false;
        }
        if(capitalist instanceof FatCat){
        	if(directory.containsKey(capitalist)){
        		return true;
        	}
    	}
        if(capitalist instanceof WageSlave && capitalist.hasParent()){
        	if (directory.get(capitalist.getParent())!=null){
        		if(directory.get(capitalist.getParent()).contains(capitalist)){
        				return true;
        			}
        		}
        }
        return false;
    }


    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
       
    	Set<Capitalist> capitalists =new HashSet<Capitalist>();
        Set<FatCat> fatCats = directory.keySet();
        
        for(Capitalist fat : fatCats){
        	for(Capitalist cap : directory.get(fat)){
        		capitalists.add(cap);
        	}
        }
        capitalists.addAll(fatCats);
       
        return capitalists;
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
        
    	Set<FatCat> returnParents = new HashSet<FatCat>();
        returnParents.addAll(directory.keySet());
       
        return returnParents;
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
    	
    	Set<Capitalist> returnChildren = new HashSet<Capitalist>();
       
    	if(directory.containsKey(fatCat)){
        	returnChildren.addAll(directory.get(fatCat));
    	}
        return returnChildren;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
        
    	Map<FatCat, Set<Capitalist>> hierarchy = new HashMap<FatCat, Set<Capitalist>>(); 
        Set<FatCat> parents = getParents();
        
        for(FatCat cat : parents){
        	hierarchy.put(cat, getChildren(cat));
        		
        }
        return hierarchy;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {
        
    	List<FatCat> parentChain = new ArrayList<FatCat>();
        
    	if(capitalist == null){
        	return parentChain;
        }
        while(capitalist.getParent() != null){
        	if(!directory.containsKey(capitalist.getParent())){
        		return parentChain;
        	}
        	parentChain.add(capitalist.getParent());
        	capitalist = capitalist.getParent();
        }
        return parentChain;
    }
}
