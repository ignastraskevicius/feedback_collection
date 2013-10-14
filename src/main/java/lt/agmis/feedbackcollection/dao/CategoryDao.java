package lt.agmis.feedbackcollection.dao;

import lt.agmis.feedbackcollection.domain.Category;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/3/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryDao {
    List<Category> getCategories();

    Category storeCategory(Category category);

    void deleteCategory(Category category);


}
