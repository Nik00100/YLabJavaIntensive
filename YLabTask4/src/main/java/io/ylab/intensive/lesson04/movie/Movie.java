package io.ylab.intensive.lesson04.movie;

public class Movie {
    private Integer year;
    private Integer length;
    private String title;
    private String subject;
    private String actors;
    private String actress;
    private String director;
    private Integer popularity;
    private Boolean awards;

    public Integer getYear() {
        return year;
    }

    public Integer getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getActors() {
        return actors;
    }

    public String getActress() {
        return actress;
    }

    public String getDirector() {
        return director;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Boolean getAwards() {
        return awards;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "year=" + year +
                ", length=" + length +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", actors='" + actors + '\'' +
                ", actress='" + actress + '\'' +
                ", director='" + director + '\'' +
                ", popularity=" + popularity +
                ", awards=" + awards +
                '}';
    }
}
