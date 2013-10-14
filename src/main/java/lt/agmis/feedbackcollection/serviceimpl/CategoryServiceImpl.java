package lt.agmis.feedbackcollection.serviceimpl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import lt.agmis.feedbackcollection.dao.CategoryDao;
import lt.agmis.feedbackcollection.domain.Category;
import lt.agmis.feedbackcollection.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/3/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> getCategories() {
        return categoryDao.getCategories();
    }

    @Override
    public int storeCategory(Category category) {
        categoryDao.storeCategory(category);
        return category.getId();
    }

    @Override
    public void deleteCategory(int id) {
        Category persistentCategory = findItemWithId(categoryDao.getCategories(), id);
        categoryDao.deleteCategory(persistentCategory);
    }

    @Override
    public void updateCategory(int id, Category category) {
        Category persistentCategory = findItemWithId(categoryDao.getCategories(), id);
        persistentCategory.setName(category.getName());
    }

    private Category findItemWithId(List<Category> categoryList, final int id) {
        return Iterables.find(categoryList, new Predicate<Category>() {
            @Override
            public boolean apply(Category category) {
                return category.getId() == id;
            }
        }, null);
    }
}
