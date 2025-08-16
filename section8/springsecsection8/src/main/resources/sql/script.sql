drop table authorities;
drop table users;
drop table customer;

create table `customer`
(
    `customer_id`   int          not null auto_increment,
    `name`          varchar(100) not null,
    `email`         varchar(100) not null,
    `mobile_number` varchar(20)  not null,
    `pwd`           varchar(500) not null,
    `role`          varchar(100) not null,
    `create_dt`     date default null,
    primary key (`customer_id`)
);

insert into `customer` (`name`, `email`, `mobile_number`, `pwd`, `role`, `create_dt`)
values ('Happy', 'happy@example.com', '5334122365',
        '{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m', 'admin', curdate());

create table `accounts`
(
    `customer_id`    int          not null,
    `account_number` int          not null,
    `account_type`   varchar(100) not null,
    `branch_address` varchar(200) not null,
    `create_dt`      date default null,
    primary key (`account_number`),
    key `customer_id` (`customer_id`),
    constraint `customer_ibfk_1` foreign key (`customer_id`) references `customer` (`customer_id`) on delete cascade
);

insert into `accounts` (`customer_id`, `account_number`, `account_type`, `branch_address`, `create_dt`)
values (1, 1865764534, 'Savings', '123 Main Street, New York', curdate());

create table `account_transactions`
(
    `transaction_id`      varchar(200) not null,
    `account_number`      int          not null,
    `customer_id`         int          not null,
    `transaction_dt`      date         not null,
    `transaction_summary` varchar(200) not null,
    `transaction_type`    varchar(100) not null,
    `transaction_amt`     int          not null,
    `closing_balance`     int          not null,
    `create_dt`           date default null,
    primary key (`transaction_id`),
    key `customer_id` (`customer_id`),
    key `account_number` (`account_number`),
    constraint `accounts_ibfk_2` foreign key (`account_number`) references `accounts` (`account_number`) on delete cascade,
    constraint `acct_user_ibfk_1` foreign key (`customer_id`) references `customer` (`customer_id`) on delete cascade
);



insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 7 day), 'Coffee Shop', 'Withdrawal', 30, 34500,
        date_sub(curdate(), interval 7 day));

insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 6 day), 'Uber', 'Withdrawal', 100, 34400,
        date_sub(curdate(), interval 6 day));

insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 5 day), 'Self Deposit', 'Deposit', 500, 34900,
        date_sub(curdate(), interval 5 day));

insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 4 day), 'Ebay', 'Withdrawal', 600, 34300,
        date_sub(curdate(), interval 4 day));

insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 2 day), 'OnlineTransfer', 'Deposit', 700, 35000,
        date_sub(curdate(), interval 2 day));

insert into `account_transactions` (`transaction_id`, `account_number`, `customer_id`, `transaction_dt`,
                                    `transaction_summary`, `transaction_type`, `transaction_amt`,
                                    `closing_balance`, `create_dt`)
values (uuid(), 1865764534, 1, date_sub(curdate(), interval 1 day), 'Amazon.com', 'Withdrawal', 100, 34900,
        date_sub(curdate(), interval 1 day));


create table `loans`
(
    `loan_number`        int          not null auto_increment,
    `customer_id`        int          not null,
    `start_dt`           date         not null,
    `loan_type`          varchar(100) not null,
    `total_loan`         int          not null,
    `amount_paid`        int          not null,
    `outstanding_amount` int          not null,
    `create_dt`          date default null,
    primary key (`loan_number`),
    key `customer_id` (`customer_id`),
    constraint `loan_customer_ibfk_1` foreign key (`customer_id`) references `customer` (`customer_id`) on delete cascade
);

insert into `loans` (`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`,
                     `create_dt`)
values (1, '2020-10-13', 'Home', 200000, 50000, 150000, '2020-10-13');

insert into `loans` (`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`,
                     `create_dt`)
values (1, '2020-06-06', 'Vehicle', 40000, 10000, 30000, '2020-06-06');

insert into `loans` (`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`,
                     `create_dt`)
values (1, '2018-02-14', 'Home', 50000, 10000, 40000, '2018-02-14');

insert into `loans` (`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`,
                     `create_dt`)
values (1, '2018-02-14', 'Personal', 10000, 3500, 6500, '2018-02-14');

create table `cards`
(
    `card_id`          int          not null auto_increment,
    `card_number`      varchar(100) not null,
    `customer_id`      int          not null,
    `card_type`        varchar(100) not null,
    `total_limit`      int          not null,
    `amount_used`      int          not null,
    `available_amount` int          not null,
    `create_dt`        date default null,
    primary key (`card_id`),
    key `customer_id` (`customer_id`),
    constraint `card_customer_ibfk_1` foreign key (`customer_id`) references `customer` (`customer_id`) on delete cascade
);

insert into `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`,
                     `create_dt`)
values ('4565XXXX4656', 1, 'Credit', 10000, 500, 9500, curdate());

insert into `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`,
                     `create_dt`)
values ('3455XXXX8673', 1, 'Credit', 7500, 600, 6900, curdate());

insert into `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`,
                     `create_dt`)
values ('2359XXXX9346', 1, 'Credit', 20000, 4000, 16000, curdate());

create table `notice_details`
(
    `notice_id`      int          not null auto_increment,
    `notice_summary` varchar(200) not null,
    `notice_details` varchar(500) not null,
    `notice_beg_dt`   date         not null,
    `notice_end_dt`   date default null,
    `create_dt`      date default null,
    `update_dt`      date default null,
    primary key (`notice_id`)
);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('Home Loan Interest rates reduced',
        'Home loan interest rates are reduced as per the goverment guidelines. The updated rates will be effective immediately',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('Net Banking Offers',
        'Customers who will opt for Internet banking while opening a saving account will get a $50 amazon voucher',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('Mobile App Downtime',
        'The mobile application of the EazyBank will be down from 2AM-5AM on 12/05/2020 due to maintenance activities',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('E Auction notice',
        'There will be a e-auction on 12/08/2020 on the Bank website for all the stubborn arrears.Interested parties can participate in the e-auction',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('Launch of Millennia Cards',
        'Millennia Credit Cards are launched for the premium customers of EazyBank. With these cards, you will get 5% cashback for each purchase',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

insert into `notice_details` (`notice_summary`, `notice_details`, `notice_beg_dt`, `notice_end_dt`, `create_dt`,
                              `update_dt`)
values ('COVID-19 Insurance',
        'EazyBank launched an insurance policy which will cover COVID-19 expenses. Please reach out to the branch for more details',
        curdate() - interval 30 day, curdate() + interval 30 day, curdate(), null);

create table `contact_messages`
(
    `contact_id`    varchar(50)   not null,
    `contact_name`  varchar(50)   not null,
    `contact_email` varchar(100)  not null,
    `subject`       varchar(500)  not null,
    `message`       varchar(2000) not null,
    `create_dt`     date default null,
    primary key (`contact_id`)
);