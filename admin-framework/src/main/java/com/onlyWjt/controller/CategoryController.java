package com.onlyWjt.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.onlyWjt.domain.dto.AddCategoryDto;
import com.onlyWjt.domain.dto.CategoryDto;
import com.onlyWjt.domain.dto.TagListDto;
import com.onlyWjt.domain.entity.Category;
import com.onlyWjt.domain.entity.ResponseResult;
import com.onlyWjt.domain.view.CategoryVo;
import com.onlyWjt.domain.view.ExcelCategoryVo;
import com.onlyWjt.domain.view.PageVo;
import com.onlyWjt.enums.AppHttpCodeEnum;
import com.onlyWjt.service.CategoryService;
import com.onlyWjt.utils.BeanCopyUtils;
import com.onlyWjt.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult getAllCategory(){
        List<CategoryVo> categoryVoList = categoryService.getAllCategory();
        return ResponseResult.okResult(categoryVoList);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse httpServletResponse){
        try {
            //设置请求头
            WebUtils.setDownLoadHeader("分类.xlsx", httpServletResponse);
            //获取需要导出的数据
            List<Category> allCategory = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVo = BeanCopyUtils.copyBeanList(allCategory, ExcelCategoryVo.class);
            //把数据写入到excel
            // 这里需要设置不关闭流
            EasyExcel.write(httpServletResponse.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类")
                    .doWrite(excelCategoryVo);

        } catch (Exception e) {
            //如果出现异常给出提示
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
//            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public ResponseResult getCateGoryList(Integer pageNum, Integer pageSize, String name, String status){
        return categoryService.getCateGoryList(pageNum, pageSize, name, status);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.updateCategory(addCategoryDto);
    }
    @GetMapping("{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }
    @DeleteMapping()
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id){
        return categoryService.deleteCategoryById(id);
    }
}
