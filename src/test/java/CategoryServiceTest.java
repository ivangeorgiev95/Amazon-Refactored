import com.amazon.AmazonApplication;
import com.amazon.domain.Category;
import com.amazon.repository.CategoryRepository;
import com.amazon.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AmazonApplication.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void clear(){
        categoryRepository.deleteAll();
    }
    @Test
    public void findParentCategoryTest(){
        Category parentCategory = new Category();
        parentCategory.setName("Parent category");
        categoryRepository.save(parentCategory);
        List<Category> result = categoryService.getAllParentCategories();
        assertTrue( result.size() == 1 && result.get(0).getName().equals("Parent category") && result.get(0).getParentCategory() == null);
    }




}
