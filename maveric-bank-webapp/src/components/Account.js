import React, { useEffect, useState } from 'react'
import './Account.css'
import { useLocation } from 'react-router-dom'
import axios from 'axios';

function Account(props) {
    const location = useLocation();
    const [userAccountBalance, setUserAccountBalance] = useState([])
    const [accountTransaction, setAccountTransaction] = useState([])
    var accountBalance = 0;
    var closingBalance = 0;
    var initialBalance = 0;

    var authToken = localStorage.getItem("token");
    var loggedInUser = JSON.parse(localStorage.getItem("user"));
    const loggedInUserAccount = location.state?.data;

    const configForUserAccount = {
        headers: { Authorization: `Bearer ${authToken}`,
                   headerUserId:  loggedInUser.id
                }
    };

    function timeout() {
        window.alert("Login timeout");
        window.location.href = '/';        
        localStorage.removeItem("token");
        localStorage.removeItem("user");
    }

    useEffect(()=>{
        axios.get("api/v1/accounts/" + loggedInUserAccount._id + "/balances", configForUserAccount)
        .then(response => setUserAccountBalance(response.data))
        .catch(err => {
            if(err.response.data.code === "403"){
                timeout();
            }
        });
        // eslint-disable-next-line

        axios.get("api/v1/accounts/" + loggedInUserAccount._id + "/transactions?page=0&pageSize=100", configForUserAccount)
        .then(response => setAccountTransaction(response.data))
        .catch(err => {
            if(err.response.data.code === "403"){
                timeout();
            }
        });
        // eslint-disable-next-line
    }, []);

    if(userAccountBalance.code === "404") {
        accountBalance = 0;
    } else {
        accountBalance = userAccountBalance.amount;
    }

    var balanceCreationDate = new Date(userAccountBalance.createdAt)

    accountTransaction.forEach(e => {
        if(e.type === "CREDIT") {
            initialBalance = e.amount + initialBalance;
        } else {
            initialBalance = initialBalance - e.amount;
        }
    })

    initialBalance = userAccountBalance.amount - initialBalance;
    closingBalance = initialBalance;

    return (
        <div className='account'>
            <div className='account-userInfo'>
                <table className='account-table-userInfo'>
                    <tr className='account-table-row-userInfo'>
                        <td className='accountUserDetailField'>Name:</td>
                        <td className='accountUserDetail'>{loggedInUser.firstName} {loggedInUser.lastName}</td>
                    </tr>
                    <tr className='account-table-row-userInfo'>
                        <td className='accountUserDetailField'>Email:</td>
                        <td className='accountUserDetail'>{loggedInUser.email}</td>
                    </tr>
                    <tr className='account-table-row-userInfo'>
                        <td className='accountUserDetailField'>Account Type:</td>
                        <td className='accountUserDetail'>{(loggedInUserAccount.type === `SAVINGS`) ? `Savings` : `Current`}</td>
                    </tr>
                    <tr className='account-table-row-userInfo'>
                        <td className='accountUserDetailField'>Account Number:</td>
                        <td className='accountUserDetail'>{loggedInUserAccount._id}</td>
                    </tr>
                    <tr className='account-table-row-userInfo'>
                        <td className='accountUserDetailField'>Closing Balanace:</td>
                        <td className='accountUserDetail'>INR {accountBalance}</td>
                    </tr>
                </table>
            </div>
            <div className='account-transactionInfo'>
                <table className='account-table-transactionInfo'>
                    <tr className='account-table-row-transactionInfo'>
                        <th className='transactionDetailField'>Date</th>
                        <th className='transactionDetailField'>Transaction Number</th>
                        <th className='transactionDetailField'>Withdrawal</th>
                        <th className='transactionDetailField'>Deposit</th>
                        <th className='transactionDetailField'>Closing Balance</th>    
                    </tr>
                    {
                        accountTransaction.length > 0  ? (
                        <>
                        <tr className='account-table-row-transactionInfo'>
                            <td className='transactionDetail'>{balanceCreationDate.toLocaleDateString()}</td>
                            <td className='transactionDetail'>{userAccountBalance._id}</td>
                            <td className='transactionDetail'></td>
                            <td className='transactionDetail'>{initialBalance}</td>
                            <td className='transactionDetail'>{initialBalance}</td>
                        </tr>
                        {
                        accountTransaction.map(function (element){

                            var createdDate = new Date(element.createdAt);
                            var withdarwal = null;
                            var deposit = null;

                            if(element.type === "CREDIT"){
                                deposit = element.amount;
                                closingBalance = closingBalance + deposit;
                            } else {
                                withdarwal = element.amount;
                                closingBalance = closingBalance - withdarwal;
                            }

                            return (
                                <tr className='account-table-row-transactionInfo'>
                                    <td className='transactionDetail'>{createdDate.toLocaleDateString()}</td>
                                    <td className='transactionDetail'>{element._id}</td>
                                    <td className='transactionDetail'>{withdarwal}</td>
                                    <td className='transactionDetail'>{deposit}</td>
                                    <td className='transactionDetail'>{closingBalance}</td>
                                </tr>
                            )})  
                            }                            
                            
                            </>
                        )
                        :
                        <tr className='dashboard-table-row-accountInfo'>
                            <td className='accountDetail' colSpan="5">No transactions found</td>
                        </tr>
                    }
                </table>
            </div>
        </div>
    )
}

export default Account