import React, { useState } from "react";
import "./Signup.css";
import { Link } from "react-router-dom";
import axios from 'axios';
import Popup from "reactjs-popup";

function Signup() {

  const [firstName, setFirstName] = useState("");
  const [middleName, setMiddleName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [address, setAddress] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [gender, setGender] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [show, setShow] = useState(false);

  const closeModal = () => setShow(false);

  const handleSubmit = (e) => {
    e.preventDefault();

    let obj = {
      firstName: firstName,
      lastName: lastName,
      middleName: middleName,
      email: email,
      phoneNumber: phoneNumber,
      address: address,
      dateOfBirth: dateOfBirth,
      gender: gender,
      password: password
    }
    
    // axios.post('/api/v1/auth/signup', obj)
    // .then(response =>{
    //     console.log(response);
    //     setShow(true);
    // })
    .catch(err => setErrorMessage(err.response.data.message));
  };

  return (
    <>
      <div className="signup">
        <form onSubmit={handleSubmit}>
          <table>
            <tr>
              <td>
                <div className="input-container">
                  <input
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    type="text"
                    placeholder="First Name"
                    id="firstName"
                    name="firstName"
                    required
                  ></input>
                </div>
              </td>
              <td>
                <div className="input-container">
                  <input
                    value={middleName}
                    onChange={(e) => setMiddleName(e.target.value)}
                    type="text"
                    placeholder="Middle Name"
                    id="middleName"
                    name="middleName"
                  ></input>
                </div>
              </td>              
              <td>
                <div className="input-container">
                  <input
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    type="text"
                    placeholder="Last Name"
                    id="lastName"
                    name="lastName"
                    required
                  ></input>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <div className="input-container">
                  <input
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    type="email"
                    placeholder="Email"
                    id="email"
                    name="email"
                    required
                  ></input>
                </div>
              </td>
              <td>
                <div className="input-container">
                  <input
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    type="text"
                    placeholder="Phone Number"
                    id="phoneNumber"
                    name="phoneNumber"
                    pattern="[1-9]{1}[0-9]{9}"
                    required
                  ></input>
                </div>
              </td>
              <td>
                <div className="input-container">
                  <input
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    type="text"
                    placeholder="Address"
                    id="address"
                    name="address"
                    required
                  ></input>
                </div>
              </td>
            </tr>
            <tr>              
            <td>
                <div className="input-container">
                  <input
                    value={dateOfBirth}
                    onChange={(e) => setDateOfBirth(e.target.value)}
                    type="date"
                    id="dateOfBirth"
                    name="dateOfBirth"
                    required
                  ></input>
                </div>
              </td>
              <td>
                <div className="input-container">
                  <div className="input-gender-male">
                    <input
                      value="MALE"
                      type="radio"
                      id="male"
                      name="gender"
                      checked={gender === "MALE"}
                      onChange={(e) => setGender(e.target.value)}
                      required
                    ></input>
                    <label htmlFor="male">Male</label>
                  </div>
                  <div className="input-gender-female">
                    <input
                      value="FEMALE"
                      type="radio"
                      id="female"
                      name="gender"
                      checked={gender === "FEMALE"}
                      onChange={(e) => setGender(e.target.value)}
                      required
                    ></input>
                    <label htmlFor="female">Female</label>
                  </div>
                </div>
              </td>
              <td>
                <div className="input-container">
                  <input
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Password"
                    required
                  ></input>
                </div>
              </td>
            </tr>
          </table>

          <div className="form-danger">{errorMessage}</div>

          <div className="button-container">
            <button className="btn" type="submit">Sign Up</button>
          </div>
        </form>

        <Popup open={show} closeOnDocumentClick onClose={closeModal}>
          <div className="modal">
            <div className="modal-text">Your account has been created successfully. Please click <Link className="login-redirect" to="/">continue</Link> to log in to your account.</div>
          </div>
        </Popup>

        <div className="input-container">
          Already have an account? <Link to="/">Login</Link>
        </div>
      </div>
    </>
  );
}

export default Signup;
