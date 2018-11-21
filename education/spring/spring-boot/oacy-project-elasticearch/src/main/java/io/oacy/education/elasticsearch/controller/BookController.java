package io.oacy.education.elasticsearch.controller;

import io.oacy.education.elasticsearch.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @auther zephyr
 * @create 2018-11-21 4:26 PM
 */

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(name = "/")
    public String index() {
        return "index";
    }

    @GetMapping("/get/book/novel")
    public ResponseEntity<Map<String, Object>> get(@RequestParam("id") String id) {
        return new ResponseEntity(bookService.get(id).getSource(), HttpStatus.OK);
    }

    @PostMapping("/add/book/novel")
    public ResponseEntity add(@RequestParam(name = "title") String title,
                              @RequestParam(name = "author") String author,
                              @RequestParam(name = "word_count") String wordCount,
                              @RequestParam(name = "publish_date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String publishDate) {
        return new ResponseEntity(bookService.add(title, author, wordCount, publishDate), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/book/novel", method = RequestMethod.DELETE)
    public ResponseEntity delete(@RequestParam("id") String id) {
        return new ResponseEntity(bookService.delete(id), HttpStatus.OK);
    }

    @PutMapping("/update/book/novel")
    public ResponseEntity update(@RequestParam("id") String id,
                                 @RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "author", required = false) String author,
                                 @RequestParam(name = "word_count", required = false) Integer wordCount,
                                 @RequestParam(name = "publish_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String publishDate) {
        return new ResponseEntity(bookService.update(id, title, author, wordCount, publishDate), HttpStatus.OK);
    }

    @PutMapping("/query/book/novel")
    public ResponseEntity query(@RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "author", required = false) String author,
                                @RequestParam(name = "gt_word_count", defaultValue = "0") Integer gtWordCount,
                                @RequestParam(name = "lt_word_count", required = false) Integer ltWordCount) {
        return new ResponseEntity(bookService.query(title, author, gtWordCount, ltWordCount), HttpStatus.OK);
    }
}
