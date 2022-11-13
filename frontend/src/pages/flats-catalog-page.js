import {Divider, Layout, Space} from "antd";
import MyHeader from "../components/main/header";
import MyFooter from "../components/main/footer";
import FlatsTable from "../components/tables/flats-table";
import {useState} from "react";
import {AddFlatForm} from "../components/forms/add-flat-form";
import {DeleteFlatForm} from "../components/forms/delete-flat-form";
import {UpdateFlatForm} from "../components/forms/update-flat-form";
import {DeleteOneFlatByViewForm} from "../components/forms/delete-one-flat-by-view-form";
import {GetAverageTimeToMetro} from "../components/response/catalog/get-average-time-to-metro";
import {GetUniqueView} from "../components/response/catalog/get-unique-view";

const {Content, Sider} = Layout;

export default function FlatsCatalogPage(){
    const [collapsed, setCollapsed] = useState(false)

    return (
        <>
            <Layout style={{minHeight: "100vh"}}>
                <MyHeader selectedMenuItem={'catalog'}/>
                <Layout>
                    <Layout
                        style={{
                            padding: '0 24px 24px',
                        }}
                    >
                        <Content>
                            <FlatsTable pageSize={5}/>
                            <Divider/>
                            <Space style={{
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                flexDirection: "column",
                            }}
                                   size={0}
                            >
                                <AddFlatForm/>
                                <Divider/>
                                <UpdateFlatForm/>
                                <Divider/>
                                <DeleteFlatForm/>
                                <Divider/>
                                <DeleteOneFlatByViewForm/>
                                <Divider/>
                                <GetAverageTimeToMetro/>
                                <Divider/>
                                <GetUniqueView/>
                            </Space>
                        </Content>
                        <MyFooter/>
                    </Layout>
                </Layout>
            </Layout>
        </>
    )
}