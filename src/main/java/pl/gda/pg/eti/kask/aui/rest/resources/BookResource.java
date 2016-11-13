package pl.gda.pg.eti.kask.aui.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import pl.gda.pg.eti.kask.aui.rest.entities.Book;
import pl.gda.pg.eti.kask.aui.rest.servlet.BooksContext;

/**
 *
 * @author psysiu
 */
@Path("/books")
public class BookResource {

    public static final String BOOK_CONTEXT = "bookContext";

    public static final String MARKED_BOOKS = "markedBooks";

    @Context
    ServletContext context;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listBooks() {
        return getBookContext().findAllBooks();
    }

    @GET
    @Path("marked/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listMarkedBooks() {
        return getMarkedBooks();
    }

    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public Book findBook(@DefaultValue("-1") @QueryParam("book_id") Integer bookId) {
        return getBookContext().findBook(bookId);
    }

    @GET
    @Path("mark")
    public void markBook(@DefaultValue("-1") @QueryParam("marked_book_id") Integer markedBookId) {
        List<Book> markedBooks = getMarkedBooks();
        Book markedBook = getBookContext().findBook(markedBookId);
        if (!markedBooks.contains(markedBook)) {
            markedBooks.add(markedBook);
        }
    }

    @POST
    @Path("save")
    public void saveBook(@FormParam("id") Integer id, @FormParam("title") String title, @FormParam("author") String author) throws IOException {
        Book book = new Book(id, author, title);
        getBookContext().saveBook(book);
        response.sendRedirect("../../list_books.html");

    }

    private BooksContext getBookContext() {
        BooksContext bookContext = (BooksContext) context.getAttribute(BOOK_CONTEXT);
        if (bookContext == null) {
            bookContext = new BooksContext();
            context.setAttribute(BOOK_CONTEXT, bookContext);
        }
        return bookContext;
    }

    private List<Book> getMarkedBooks() {
        List<Book> markedBooks = (List<Book>) request.getSession().getAttribute(MARKED_BOOKS);
        if (markedBooks == null) {
            markedBooks = new ArrayList<>();
            request.getSession().setAttribute(MARKED_BOOKS, markedBooks);
        }
        return markedBooks;
    }
}
