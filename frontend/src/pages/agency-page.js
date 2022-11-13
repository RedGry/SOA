import {Layout} from "antd";
import {Content} from "antd/es/layout/layout";
import MyFooter from "../components/main/footer";
import MyHeader from "../components/main/header";

export function AgencyPage(){
    return (
        <>
            <Layout>
                <MyHeader/>
                <Content>

                </Content>
                <MyFooter/>
            </Layout>
        </>
    )
}