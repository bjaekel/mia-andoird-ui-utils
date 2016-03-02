package de.sevenfactory.mia.modules.irregulargrid;

import java.util.ArrayList;
import java.util.List;

import de.sevenfactory.mia.modules.feed.FeedRepository;
import de.sevenfactory.mia.modules.paging.MockPage;
import de.sevenfactory.mia.modules.paging.PageModel;

public class MockIrregularGridRepository implements FeedRepository {

    private int mFailureCounter;

    @Override
    public PageModel<IrregularGridModel> fetchFeedItems(int amount)  {

        if(mFailureCounter < 3) {
            mFailureCounter ++;
            throw new RuntimeException();
        }

        List<IrregularGridModel> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add(new IrregularGridModel() {

                @Override
                public String getId() {
                    return "2623829";
                }

                @Override
                public String getTitle() {
                    return "Show Title abc123";
                }

                @Override
                public String getSubTitle() {
                    return null;
                }

                @Override
                public ImageListModel getImages() {
                    ImageListModel images = new ImageListModel() {
                        @Override
                        public String getTeaserUrl() {
                            return null;
                        }
    
                        @Override
                        public String getCoverBig() {
                            return null;
                        }
                    };
                    
                    return images;
                }

                @Override
                public long getDuration() {
                    return 0;
                }

                @Override
                public long getAirDate() {
                    return 0;
                }

                @Override
                public String getBrand() {
                    return null;
                }

                @Override
                public boolean usesColoredBrandIcon() {
                    return false;
                }
            });
        }

        return new MockPage<>(items.size(), items);
    }
}
