import React, { useEffect, useState } from 'react';
import './Login.css';
import { Link } from 'react-router-dom';
import axios from 'axios';

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [currentUser, setCurrentUser] = useState();
    const [authToken, setAuthToken] = useState();

    useEffect(()=> {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
    }, [])

    const handleSubmit = (e) => {
        e.preventDefault();

        let obj = {
            email: email,
            password: password
        }

        axios.post('/api/v1/auth/login', obj)
        .then(response =>{
            setAuthToken(response.data.token);

            setCurrentUser(response.data.user);

            window.location.href = '/dashboard';

        })
        .catch(err => setErrorMessage(err.response.data.message));

        
    }

    localStorage.setItem("token", authToken);
    localStorage.setItem("user", JSON.stringify(currentUser));

  return (
    <>
        <div className='login'>
            <form onSubmit={handleSubmit}>
                <div className='input-container'>
                    <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" placeholder='Email' id="email" name='email' required></input>
                </div>

                <div className='input-container'>
                    <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" id="password" name="password" placeholder='Password' required></input>
                </div>

                <div className="form-danger">{errorMessage}</div>
                
                <div className='button-container'>
                    <button className='btn' type='submit'>Login</button>
                </div>
            </form>
            <div className='input-container'>Don't have an account? <Link to="/signup">Signup</Link></div>
        </div>
    </>
  )
}

export default Login