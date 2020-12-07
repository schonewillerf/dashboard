package hu.adsd.dashboard.burndown;

import java.time.LocalDate;
import java.util.Iterator;

/**
 * Cool design pattern that will help us a lot explaining inheritance, polymorphism and design patterns
 *
 * Check https://en.wikipedia.org/wiki/Iterator_pattern
 */
public class SprintIterator implements Iterable<LocalDate>
{
    private LocalDate startDate;
    private LocalDate endDate;

    public SprintIterator( String startDateString, String endDateString )
    {
        this.startDate = LocalDate.parse( startDateString );
        this.endDate = LocalDate.parse( endDateString );
    }

    @Override
    public Iterator iterator()
    {
        return new SprintDayIterator();
    }

    class SprintDayIterator implements Iterator<LocalDate>
    {
        @Override
        public boolean hasNext()
        {
            return (startDate.compareTo( endDate ) <= 0);
        }

        @Override
        public LocalDate next()
        {
            startDate = startDate.plusDays( 1 );

            return startDate.minusDays( 1 );
        }
    }
}
