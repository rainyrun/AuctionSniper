package sniper;

public enum SniperState {
    JOINING {
        @Override
        public SniperState whenAuctionClosed() {
            return LOST;
        }
    },
    BIDDING {
        @Override
        public SniperState whenAuctionClosed() {
            return LOST;
        }
    },
    WINNING {
        @Override
        public SniperState whenAuctionClosed() {
            return WON;
        }
    },
    LOSING {
        @Override
        public SniperState whenAuctionClosed() {
            return LOST;
        }
    },
    LOST,
    WON;

    public SniperState whenAuctionClosed() {
//        switch (this) {
//            case BIDDING:
//            case JOINING:
//                return LOST;
//            case WINNING:
//                return WON;
//            case LOST:
//            case WON:
//                throw new IllegalStateException("Auction is already closed.");
//        }
        throw new IllegalStateException("Auction is already closed.");
    }
}