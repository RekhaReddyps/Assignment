import React, { useEffect, useState } from 'react'
import './Dashboard.css'
import axios from 'axios';
import { Link } from 'react-router-dom';

function Dashboard() {

    const [userAccount, setUserAccount] = useState([]);

    var authToken = localStorage.getItem("token");
    var loggedInUser = JSON.parse(localStorage.getItem("user"));

    var date = new Date(loggedInUser.dateOfBirth);

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
        axios.get("api/v1/customers/" + loggedInUser.id + "/accounts?page=0&pageSize=100", configForUserAccount)
        .then(response => setUserAccount(response.data))
        .catch(err => {
            if(err.response.data.code === "403"){
                timeout();
            }
        });
        // eslint-disable-next-line
    }, []);

    return (
        <div className='dashboard'>
            <div className='dashboard-userInfo'>
                <table className='dashboard-table-userInfo'>
                    <tr className='dashboard-table-row-userInfo'>
                        <td className='userDetailField'>Name:</td>
                        <td className='userDetail'>{loggedInUser.firstName} {loggedInUser.lastName}</td>
                    </tr>
                    <tr className='dashboard-table-row-userInfo'>
                        <td className='userDetailField'>Email:</td>
                        <td className='userDetail'>{loggedInUser.email}</td>
                    </tr>
                    <tr className='dashboard-table-row-userInfo'>
                        <td className='userDetailField'>Gender:</td>
                        <td className='userDetail'>{(loggedInUser.gender === `FEMALE`) ? `Female` : `Male`}</td>
                    </tr>
                    <tr className='dashboard-table-row-userInfo'>
                        <td className='userDetailField'>Phone Number:</td>
                        <td className='userDetail'>{loggedInUser.phoneNumber}</td>
                    </tr>
                    <tr className='dashboard-table-row-userInfo'>
                        <td className='userDetailField'>Date of Birth:</td>
                        <td className='userDetail'>{date.toLocaleDateString()}</td>
                    </tr>
                </table>
            </div>
            <div className='dashboard-accountInfo'>
                <table className='dashboard-table-accountInfo'>
                    <tr className='dashboard-table-row-accountInfo'>
                        <th className='accountDetailField'>Account Number</th>
                        <th className='accountDetailField'>Account Type</th>
                        <th className='accountDetailField accountAction'>Action</th>
                    </tr>
                    
                    {
                        userAccount.length > 0  ?
                        userAccount.map(element => (
                            <tr className='dashboard-table-row-accountInfo'>
                                <td className='accountDetail'>{element._id}</td>
                                <td className='accountDetail'>{(element.type === `SAVINGS`) ? `Savings` : `Current`}</td>
                                <td className='accountDetail'><Link to="/account" state={{data: element}}>View</Link></td>
                            </tr>
                        )) :

                        <tr className='dashboard-table-row-accountInfo'>
                            <td className='accountDetail textcenter' colSpan="3">No Accounts created</td>
                        </tr>
                    }
                </table>
            </div>
        </div>
    )
}

export default Dashboard