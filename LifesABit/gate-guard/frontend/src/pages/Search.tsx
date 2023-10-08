import React from 'react';
import {useState, useEffect} from 'react';
import GuardNavBar from '../components/GuardNavbar';
import "./Search.scss";
import Table from "./Table";
import connector from '../utils/axiosConfig';
import { Form } from 'react-bootstrap';



interface LoadMembersRequest {
    sessionKey: string;
}

interface LoadMembersResponse {
    memberList: Member[];
}

interface Member {
    id?: string;
    first_name?: string;
    last_name?: string;
    username?: string;
    email?: string;
    member_type?: "MEMBER" | "ADMIN" | "OWNER";
}

interface SearchProps {
  isOwner: boolean;
}

const Search = (props: SearchProps) => {

    const [listOfMembers, setListOfMembers] = useState<Member[]>([]);
    const [query, setQuery] = useState("");
    const search = (data : any) => {
        return data.filter((item : any)=> item.first_name.toLowerCase().includes(query.toLowerCase()) ||
                                          item.last_name.toLowerCase().includes(query.toLowerCase()) ||
                                          item.username.toLowerCase().includes(query.toLowerCase()) ||
                                          item.member_type.toLowerCase().includes(query.toLowerCase()) ||
                                          item.email.toLowerCase().includes(query.toLowerCase()));
    }

    const onSubmit = async () => {
        try {
          await connector.post('load-members', {})
          .then((result) => {
            if (result.status == 200) {
                setListOfMembers(result.data.memberList);
            }
          });
        } catch (e: any) {
          if (e.message.includes("401")) {
            //setWrongLogin(true); TODO: Write message not admin.
          } else {
            console.log(e);
          }
        }
      }

    useEffect(() => {
        onSubmit();
    },[]);
    
    return (
        <div className="SearchButton">
            <Form.Control type="text" 
            placeholder="Search..." 
            className="search" 
            onChange={(e) => setQuery(e.target.value)} 
            />
            <div className="tableData"><Table data={{
              "list": search(listOfMembers),
              "isOwner": props.isOwner
              }}/></div>
        </div>
    );
};

export default Search;